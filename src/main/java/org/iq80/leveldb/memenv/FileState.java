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
package org.iq80.leveldb.memenv;

import org.iq80.leveldb.util.Slice;

import java.io.IOException;
import java.util.Arrays;

/**
 * File content.
 */
class FileState {
    private static final int BLOCK_SIZE = 8 * 1024;
    // TODO switch to RW lock. we need concurrent read only
    private final Object lock = new Object();
    //file lock
    private boolean locked = false;
    private int size = 0;
    private byte[][] content;

    /**
     * Return current file size
     *
     * @return current file size
     */
    public long length() {
        synchronized (lock) {
            return size;
        }
    }

    /**
     * Delete all content of file
     *
     * @return slef file
     */
    public FileState truncate() {
        synchronized (lock) {
            content = null;
            size = 0;
        }
        return this;
    }

    /**
     * Read some content from file
     *
     * @param offset data offset
     * @param n      at most number of bytes to read
     * @return read bytes or {@code null} if EOF is reached
     * @throws IOException on any "IO" error
     */
    public byte[] read(long offset, int n) throws IOException {
        synchronized (lock) {
            if (offset > size) {
                throw new IOException("Offset greater than file size.");
            }
            long available = size - offset;
            if (n != 0 && available == 0) {
                return null; //EOF
            }
            if (n > available) {
                n = (int) available;
            }
            if (n == 0) {
                return new byte[0];
            }
            //Preconditions.checkArgument(offset / BLOCK_SIZE <= Integer.MAX_VALUE, "Invalid offset");
            int block = (int) (offset / BLOCK_SIZE);
            int blockOffset = (int) (offset % BLOCK_SIZE);
            int bytesToCopy = n;
            int dst = 0;
            byte[] output = new byte[n];
            while (bytesToCopy > 0) {
                int avail = BLOCK_SIZE - blockOffset;
                if (avail > bytesToCopy) {
                    avail = bytesToCopy;
                }
                System.arraycopy(content[block], blockOffset, output, dst, avail);
                bytesToCopy -= avail;
                dst += avail;
                block++;
                blockOffset = 0;
            }
            return output;
        }
    }

    /**
     * Add content at the end of the file
     *
     * @param data content to append
     */
    public void append(Slice data) {
        int srcLen = data.length();
        //avoid 2 copy
        byte[] src = data.getRawArray();
        int srcPos = data.getRawOffset();
        synchronized (lock) {
            while (srcLen > 0) {
                int avail;
                int offset = size % BLOCK_SIZE;

                if (offset != 0) {
                    // There is some room in the last block.
                    avail = BLOCK_SIZE - offset;
                } else {
                    // No room in the last block; push new one.
                    addBlock(new byte[BLOCK_SIZE]);
                    avail = BLOCK_SIZE;
                }

                if (avail > srcLen) {
                    avail = srcLen;
                }
                System.arraycopy(src, srcPos, content[content.length - 1], offset, avail);
                srcLen -= avail;
                srcPos += avail;
                size += avail;
            }
        }
    }

    private void addBlock(byte[] bytes) {
        if (content == null) {
            content = new byte[1][];
        } else {
            content = Arrays.copyOf(content, content.length + 1);
        }
        content[content.length - 1] = bytes;
    }

    /**
     * {@code true} If file locked
     *
     * @return {@code true} If file locked
     */
    public boolean isLocked() {
        synchronized (lock) {
            return locked;
        }
    }

    /**
     * Set new locked file state
     *
     * @param locked new lock state
     */
    public void setLocked(boolean locked) {
        synchronized (lock) {
            this.locked = locked;
        }
    }
}
