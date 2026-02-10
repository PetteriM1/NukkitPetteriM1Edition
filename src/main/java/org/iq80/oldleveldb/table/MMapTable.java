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
package org.iq80.oldleveldb.table;

import com.google.common.base.Preconditions;
import org.iq80.leveldb.util.Closeables;
import org.iq80.oldleveldb.util.ByteBufferSupport;
import org.iq80.oldleveldb.util.Slice;
import org.iq80.oldleveldb.util.Slices;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Comparator;
import java.util.concurrent.Callable;

import static org.iq80.leveldb.CompressionType.*;

public class MMapTable
        extends Table {
    private MappedByteBuffer data;

    public MMapTable(String name, FileChannel fileChannel, Comparator<Slice> comparator, boolean verifyChecksums)
            throws IOException {
        super(name, fileChannel, comparator, verifyChecksums);
        Preconditions.checkArgument(fileChannel.size() <= Integer.MAX_VALUE);
    }

    private static class Closer
            implements Callable<Void> {
        private final Closeable closeable;
        private final MappedByteBuffer data;

        public Closer(String name, Closeable closeable, MappedByteBuffer data) {
            this.closeable = closeable;
            this.data = data;
        }

        public Void call() {
            ByteBufferSupport.unmap(data);
            Closeables.closeQuietly(closeable);
            return null;
        }
    }

    @Override
    public Callable<?> closer() {
        return new Closer(name, fileChannel, data);
    }

    @Override
    protected Footer init()
            throws IOException {
        long size = fileChannel.size();
        data = fileChannel.map(MapMode.READ_ONLY, 0, size);
        Slice footerSlice = Slices.copiedBuffer(data, (int) size - Footer.ENCODED_LENGTH, Footer.ENCODED_LENGTH);
        return Footer.readFooter(footerSlice);
    }

    public static ByteBuffer read(MappedByteBuffer data, int offset, int length) {
        int newPosition = data.position() + offset;
        return (ByteBuffer) ((ByteBuffer) data).duplicate().order(ByteOrder.LITTLE_ENDIAN).clear().limit(newPosition + length).position(newPosition);
    }

    @SuppressWarnings({"NonPrivateFieldAccessedInSynchronizedContext", "AssignmentToStaticFieldFromInstanceMethod"})
    @Override
    protected Block readBlock(BlockHandle blockHandle)
            throws IOException {
        // read block trailer
        BlockTrailer blockTrailer = BlockTrailer.readBlockTrailer(Slices.copiedBuffer(this.data,
                (int) blockHandle.getOffset() + blockHandle.getDataSize(),
                BlockTrailer.ENCODED_LENGTH));

// todo re-enable crc check when ported to support direct buffers
//        // only verify check sums if explicitly asked by the user
//        if (verifyChecksums) {
//            // checksum data and the compression type in the trailer
//            PureJavaCrc32C checksum = new PureJavaCrc32C();
//            checksum.update(data.getRawArray(), data.getRawOffset(), blockHandle.getDataSize() + 1);
//            int actualCrc32c = checksum.getMaskedValue();
//
//            Preconditions.checkState(blockTrailer.getCrc32c() == actualCrc32c, "Block corrupted: checksum mismatch");
//        }

        // decompress data
        Slice uncompressedData;
        ByteBuffer uncompressedBuffer = read(this.data, (int) blockHandle.getOffset(), blockHandle.getDataSize());
        if (blockTrailer.getCompressionType() == ZLIB_RAW) {
            synchronized (MMapTable.class) {
                byte[] bytes = new byte[uncompressedBuffer.remaining()];
                uncompressedBuffer.get(bytes);
                uncompressedData = Slices.wrappedBuffer(cn.nukkit.utils.Zlib.inflateRaw(bytes, 0));
            }
        } else if (blockTrailer.getCompressionType() == ZLIB) {
            synchronized (MMapTable.class) {
                byte[] bytes = new byte[uncompressedBuffer.remaining()];
                uncompressedBuffer.get(bytes);
                uncompressedData = Slices.wrappedBuffer(cn.nukkit.utils.Zlib.inflate(bytes, 0));
            }
        } else if (blockTrailer.getCompressionType() == SNAPPY) {
            synchronized (MMapTable.class) {
                byte[] bytes = new byte[uncompressedBuffer.remaining()];
                uncompressedBuffer.get(bytes);
                uncompressedData = Slices.wrappedBuffer(cn.nukkit.utils.SnappyCompression.decompress(bytes, 0));
            }
        } else {
            uncompressedData = Slices.copiedBuffer(uncompressedBuffer);
        }

        return new Block(uncompressedData, comparator);
    }
}
