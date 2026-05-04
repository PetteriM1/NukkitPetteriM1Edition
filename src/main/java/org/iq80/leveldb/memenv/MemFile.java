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

import com.google.common.base.Preconditions;
import org.iq80.leveldb.env.File;

import java.util.List;
import java.util.Objects;

import static org.iq80.leveldb.memenv.MemFs.SEPARATOR;
import static org.iq80.leveldb.memenv.MemFs.SEPARATOR_CHAR;

class MemFile implements File {
    private final MemFs fs;
    private final String filename;

    private MemFile(MemFs fs, String filename) {
        Preconditions.checkArgument(fs != null, "fs null");
        Preconditions.checkArgument(filename != null && !filename.isEmpty(), "empty file name");
        this.fs = fs;
        this.filename = filename;
    }

    @Override
    public String getName() {
        int i = filename.lastIndexOf(SEPARATOR_CHAR);
        return i >= 0 ? filename.substring(i + 1) : filename;
    }

    @Override
    public MemFile getParentFile() {
        int i = filename.lastIndexOf(SEPARATOR);
        return createMemFile(fs, i > 0 ? filename.substring(0, i) : SEPARATOR);
    }

    @Override
    public String getPath() {
        return filename;
    }

    @Override
    public boolean isDirectory() {
        return fs.isDirectory(this);
    }

    @Override
    public boolean isFile() {
        return fs.isFile(this);
    }

    @Override
    public boolean canRead() {
        return fs.canRead(this);
    }

    @Override
    public MemFile child(String other) {
        Preconditions.checkArgument(other == null || other.isEmpty() || !other.contains(SEPARATOR), "Invalid file/directory name %s", other);
        return createMemFile(fs, filename + SEPARATOR_CHAR + other);
    }

    static MemFile createMemFile(MemFs fs, String filename) {
        Objects.requireNonNull(filename);
        String path = filename;
        if (!path.startsWith(MemFs.SEPARATOR)) {
            path = MemFs.SEPARATOR + path;
        }
        while (path.length() > 1 && path.endsWith(MemFs.SEPARATOR)) {
            path = path.substring(0, path.length() - 1);
        }
        return new MemFile(fs, path);
    }

    @Override
    public boolean delete() {
        return fs.delete(this);
    }

    @Override
    public boolean deleteRecursively() {
        return fs.deleteRecursively(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemFile memFile = (MemFile) o;
        return Objects.equals(filename, memFile.filename);
    }

    @Override
    public boolean exists() {
        return fs.exists(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename);
    }

    @Override
    public long length() {
        return fs.getFileState(this).map(FileState::length).orElse(0L);
    }

    @Override
    public List<File> listFiles() {
        return fs.listFiles(this);
    }

    @Override
    public boolean mkdirs() {
        return fs.mkdirs(this);
    }

    @Override
    public boolean renameTo(File dest) {
        return fs.renameTo(this, ((MemFile) dest));
    }

    @Override
    public String toString() {
        return filename;
    }
}
