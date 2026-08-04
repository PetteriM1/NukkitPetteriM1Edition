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
package org.iq80.leveldb.fileenv;

import org.iq80.leveldb.env.RandomInputFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.util.Objects;

/**
 * @author Honore Vasconcelos
 */
class UnbufferedRandomInputFile implements RandomInputFile {
    private static final int MAX_RETRY = Integer.getInteger(" org.iq80.leveldb.FileChannel.RETRY", 1000);
    private final Object lock = new Object();
    private final File file;
    private volatile FileChannel fileChannel;
    private final long size;
    private boolean closed = false;

    private UnbufferedRandomInputFile(File file, FileChannel fileChannel, long size) {
        this.file = file;
        this.fileChannel = fileChannel;
        this.size = size;
    }

    public static RandomInputFile open(File file) throws IOException {
        Objects.requireNonNull(file);
        FileChannel channel = openChannel(file);
        return new UnbufferedRandomInputFile(file, channel, channel.size());
    }

    private static FileChannel openChannel(File file) throws FileNotFoundException {
        return new FileInputStream(file).getChannel();
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public ByteBuffer read(long offset, int length) throws IOException {
        if (Thread.currentThread().isInterrupted()) {
            throw new ClosedByInterruptException(); //do no close!
        }
        ByteBuffer uncompressedBuffer = ByteBuffer.allocate(length).order(ByteOrder.LITTLE_ENDIAN);
        int maxRetry = MAX_RETRY;
        do {
            final FileChannel fc = this.fileChannel;
            try {
                fc.read(uncompressedBuffer, offset);
                if (uncompressedBuffer.hasRemaining()) {
                    throw new IOException("Could not read all the data");
                }
                uncompressedBuffer.clear();
                return uncompressedBuffer;
            } catch (ClosedByInterruptException e) {
                throw e;
            } catch (ClosedChannelException e) {
                uncompressedBuffer.clear();
                if (!reOpenChannel(fc)) {
                    throw new IOException("Channel closed by an other thread concurrently");
                }
            }
        } while (--maxRetry > 0);
        throw new IOException("Unable to reopen file after close exception");
    }

    private boolean reOpenChannel(FileChannel currentFc) throws FileNotFoundException {
        synchronized (lock) {
            if (closed) {
                //externally closed
                return false;
            }
            if (this.fileChannel == currentFc) {
                this.fileChannel = openChannel(file);
            }
        }
        return true;
    }

    @Override
    public void close() throws IOException {
        synchronized (lock) {
            if (closed) {
                return;
            }
            closed = true;
        }
        fileChannel.close();
    }

    @Override
    public String toString() {
        return "FileTableDataSource{" +
                "file='" + file + '\'' +
                ", size=" + size +
                '}';
    }
}
