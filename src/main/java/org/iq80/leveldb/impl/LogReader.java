/*
 * Copyright (C) 2011 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.iq80.leveldb.impl;

import org.iq80.leveldb.env.SequentialFile;
import org.iq80.leveldb.util.*;

import java.io.IOException;

import static org.iq80.leveldb.impl.LogChunkType.*;
import static org.iq80.leveldb.impl.LogConstants.BLOCK_SIZE;
import static org.iq80.leveldb.impl.LogConstants.HEADER_SIZE;
import static org.iq80.leveldb.impl.Logs.getChunkChecksum;

public class LogReader {
    private final SequentialFile sequentialFile;

    private final LogMonitor monitor;

    private final boolean verifyChecksums;

    /**
     * Offset at which to start looking for the first record to return
     */
    private final long initialOffset;
    private boolean resyncing;

    /**
     * Have we read to the end of the file?
     */
    private boolean eof;

    /**
     * Offset of the last record returned by readRecord.
     */
    private long lastRecordOffset;

    /**
     * Offset of the first location past the end of buffer.
     */
    private long endOfBufferOffset;

    /**
     * Scratch buffer in which the next record is assembled.
     */
    private final DynamicSliceOutput recordScratch = new DynamicSliceOutput(BLOCK_SIZE);

    /**
     * Scratch buffer for current block.  The currentBlock is sliced off the underlying buffer.
     */
    private final SliceOutput blockScratch = Slices.allocate(BLOCK_SIZE).output();

    /**
     * The current block records are being read from.
     */
    private SliceInput currentBlock = Slices.EMPTY_SLICE.input();

    /**
     * Current chunk which is sliced from the current block.
     */
    private Slice currentChunk = Slices.EMPTY_SLICE;

    public LogReader(SequentialFile sequentialFile, LogMonitor monitor, boolean verifyChecksums, long initialOffset) {
        this.sequentialFile = sequentialFile;
        this.monitor = monitor;
        this.verifyChecksums = verifyChecksums;
        this.initialOffset = initialOffset;
        this.resyncing = initialOffset > 0;
    }

    public long getLastRecordOffset() {
        return lastRecordOffset;
    }

    private boolean readNextBlock() {
        if (eof) {
            return false;
        }

        // clear the block
        blockScratch.reset();

        int readSoFar = 0;
        // read the next full block
        while (blockScratch.writableBytes() > 0) {
            try {
                int bytesRead = sequentialFile.read(blockScratch.writableBytes(), blockScratch);
                if (bytesRead < 0) { //eof
                    // no more bytes to read
                    eof = true;
                    if (blockScratch.writableBytes() > 0 && readSoFar < HEADER_SIZE) {
                        // Note that if buffer_ is non-empty, we have a truncated header at the
                        // end of the file, which can be caused by the writer crashing in the
                        // middle of writing the header. Instead of considering this an error,
                        // just report EOF.
                        currentBlock = Slices.EMPTY_SLICE.input();
                        return false;
                    }
                    break;
                }
                readSoFar += bytesRead;
                endOfBufferOffset += bytesRead;
            } catch (IOException e) {
                currentBlock = Slices.EMPTY_SLICE.input();
                reportDrop(BLOCK_SIZE, e);
                eof = true;
                return false;
            }

        }
        currentBlock = blockScratch.slice().input();
        return currentBlock.isReadable();
    }

    /**
     * Return type, or one of the preceding special values
     */
    private LogChunkType readNextChunk() {
        // clear the current chunk
        currentChunk = Slices.EMPTY_SLICE;

        // read the next block if necessary
        if (currentBlock.available() < HEADER_SIZE) {
            if (!readNextBlock()) {
                if (eof) {
                    return EOF;
                }
            }
        }

        // parse header
        int expectedChecksum = currentBlock.readInt();
        int length = currentBlock.readUnsignedByte();
        length = length | currentBlock.readUnsignedByte() << 8;
        byte chunkTypeId = currentBlock.readByte();
        LogChunkType chunkType = getLogChunkTypeByPersistentId(chunkTypeId);

        // verify length
        if (length > currentBlock.available()) {
            if (!eof) {
                int dropSize = currentBlock.available() + HEADER_SIZE;
                reportCorruption(dropSize, "bad record length");
                currentBlock = Slices.EMPTY_SLICE.input();
                return BAD_CHUNK;
            }
            // If the end of the file has been reached without reading |length| bytes
            // of payload, assume the writer died in the middle of writing the record.
            // Don't report a corruption.
            return EOF;
        }

        // skip zero length records
        if (chunkType == ZERO_TYPE && length == 0) {
            // Skip zero length record without reporting any drops since
            // such records are produced by the writing code.
            currentBlock = Slices.EMPTY_SLICE.input();
            return BAD_CHUNK;
        }

        // Skip physical record that started before initialOffset
        if (endOfBufferOffset - HEADER_SIZE - length < initialOffset) {
            currentBlock.skipBytes(length);
            return BAD_CHUNK;
        }

        // read the chunk
        currentChunk = currentBlock.readBytes(length);

        if (verifyChecksums) {
            int actualChecksum = getChunkChecksum(chunkTypeId, currentChunk);
            if (actualChecksum != expectedChecksum) {
                // Drop the rest of the buffer since "length" itself may have
                // been corrupted and if we trust it, we could find some
                // fragment of a real log record that just happens to look
                // like a valid log record.
                int dropSize = currentBlock.available() + HEADER_SIZE + length;
                currentBlock = Slices.EMPTY_SLICE.input();
                reportCorruption(dropSize, "checksum mismatch");
                return BAD_CHUNK;
            }
        }

        // Skip physical record that started before initial_offset_
        if (endOfBufferOffset - currentBlock.available() - HEADER_SIZE - length <
                initialOffset) {
            currentChunk = Slices.EMPTY_SLICE;
            return BAD_CHUNK;
        }

        // Skip unknown chunk types
        // Since this comes last so we the, know it is a valid chunk, and is just a type we don't understand
        if (chunkType == UNKNOWN) {
            reportCorruption(length, "unknown record type");
            return BAD_CHUNK;
        }

        return chunkType;
    }

    public Slice readRecord() {
        recordScratch.reset();

        // advance to the first record, if we haven't already
        if (lastRecordOffset < initialOffset) {
            if (!skipToInitialBlock()) {
                return null;
            }
        }

        // Record offset of the logical record that we're reading
        long prospectiveRecordOffset = 0;

        boolean inFragmentedRecord = false;
        while (true) {
            LogChunkType chunkType = readNextChunk();

            // ReadPhysicalRecord may have only had an empty trailer remaining in its
            // internal buffer. Calculate the offset of the next physical record now
            // that it has returned, properly accounting for its header size.
            long physicalRecordOffset = endOfBufferOffset - currentBlock.available() - HEADER_SIZE - currentChunk.length();

            if (resyncing) {
                if (chunkType == MIDDLE) {
                    continue;
                } else if (chunkType == LAST) {
                    resyncing = false;
                    continue;
                } else {
                    resyncing = false;
                }
            }

            switch (chunkType) {
                case FULL:
                    if (inFragmentedRecord) {
                        reportCorruption(recordScratch.size(), "partial record without end(1)");
                        // simply return this full block
                    }
                    recordScratch.reset();
                    prospectiveRecordOffset = physicalRecordOffset;
                    lastRecordOffset = prospectiveRecordOffset;
                    return currentChunk.copySlice();

                case FIRST:
                    if (inFragmentedRecord) {
                        reportCorruption(recordScratch.size(), "partial record without end(2)");
                        // clear the scratch and start over from this chunk
                        recordScratch.reset();
                    }
                    prospectiveRecordOffset = physicalRecordOffset;
                    recordScratch.writeBytes(currentChunk);
                    inFragmentedRecord = true;
                    break;

                case MIDDLE:
                    if (!inFragmentedRecord) {
                        reportCorruption(currentChunk.length(), "missing start of fragmented record(1)");

                        // clear the scratch and skip this chunk
                        recordScratch.reset();
                    } else {
                        recordScratch.writeBytes(currentChunk);
                    }
                    break;

                case LAST:
                    if (!inFragmentedRecord) {
                        reportCorruption(currentChunk.length(), "missing start of fragmented record(2)");

                        // clear the scratch and skip this chunk
                        recordScratch.reset();
                    } else {
                        recordScratch.writeBytes(currentChunk);
                        lastRecordOffset = prospectiveRecordOffset;
                        return recordScratch.slice().copySlice();
                    }
                    break;

                case EOF:
                    if (inFragmentedRecord) {
                        // This can be caused by the writer dying immediately after
                        // writing a physical record but before completing the next; don't
                        // treat it as a corruption, just ignore the entire logical record.
                        recordScratch.reset();
                    }
                    return null;

                case BAD_CHUNK:
                    if (inFragmentedRecord) {
                        reportCorruption(recordScratch.size(), "error in middle of record");
                        inFragmentedRecord = false;
                        recordScratch.reset();
                    }
                    break;

                default:
                    int dropSize = currentChunk.length();
                    if (inFragmentedRecord) {
                        dropSize += recordScratch.size();
                    }
                    reportCorruption(dropSize, String.format("Unexpected chunk type %s", chunkType));
                    inFragmentedRecord = false;
                    recordScratch.reset();
                    break;
            }
        }
    }

    /**
     * Reports corruption to the monitor.
     * The buffer must be updated to remove the dropped bytes prior to invocation.
     */
    private void reportCorruption(long bytes, String reason) {
        if (monitor != null) {
            monitor.corruption(bytes, reason);
        }
    }

    /**
     * Reports dropped bytes to the monitor.
     * The buffer must be updated to remove the dropped bytes prior to invocation.
     */
    private void reportDrop(long bytes, Throwable reason) {
        if (monitor != null) {
            monitor.corruption(bytes, reason);
        }
    }

    /**
     * Skips all blocks that are completely before "initial_offset_".
     * <p/>
     * Handles reporting corruption
     *
     * @return true on success.
     */
    private boolean skipToInitialBlock() {
        int offsetInBlock = (int) (initialOffset % BLOCK_SIZE);
        long blockStartLocation = initialOffset - offsetInBlock;

        // Don't search a block if we'd be in the trailer
        if (offsetInBlock > BLOCK_SIZE - 6) {
            blockStartLocation += BLOCK_SIZE;
        }

        endOfBufferOffset = blockStartLocation;

        // Skip to start of first block that can contain the initial record
        if (blockStartLocation > 0) {
            try {
                sequentialFile.skip(blockStartLocation);
            } catch (IOException e) {
                reportDrop(blockStartLocation, e);
                return false;
            }
        }

        return true;
    }
}
