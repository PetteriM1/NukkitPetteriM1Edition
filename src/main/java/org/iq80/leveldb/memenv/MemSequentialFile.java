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

import org.iq80.leveldb.env.SequentialFile;
import org.iq80.leveldb.util.SliceOutput;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;

class MemSequentialFile implements SequentialFile {
    private final FileState fileState;
    private boolean closed;
    private int index;

    public MemSequentialFile(FileState fileState) {
        this.fileState = fileState;
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

    @Override
    public int read(int atMost, SliceOutput destination) throws IOException {
        if (closed) {
            throw new ClosedChannelException();
        }
        byte[] read = fileState.read(index, atMost);
        if (read != null) {
            index += read.length;
            destination.writeBytes(read);
            return read.length;
        }
        return -1;
    }

    @Override
    public void skip(long n) throws IOException {
        if (index > fileState.length()) {
            throw new IOException("File position " + index + " is greater than file size");
        }
        long available = fileState.length() - index;
        if (n > available) {
            n = available;
        }
        index += n;
    }
}
