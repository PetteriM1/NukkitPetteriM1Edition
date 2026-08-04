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

import org.iq80.leveldb.env.File;
import org.iq80.leveldb.env.RandomInputFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

class MemRandomInputFile implements RandomInputFile {
    private final File file;
    private final FileState fileState;
    private boolean closed;

    public MemRandomInputFile(File file, FileState fileState) {
        this.file = file;
        this.fileState = fileState;
        closed = false;
    }

    @Override
    public long size() {
        return file.length();
    }

    @Override
    public ByteBuffer read(long offset, int length) throws IOException {
        if (closed) {
            throw new ClosedChannelException();
        }
        byte[] read = fileState.read(offset, length);
        if (read == null) {
            throw new IOException("Could not read all the data");
        }
        // read is already a copy
        return ByteBuffer.wrap(read);
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }
}
