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

import org.iq80.leveldb.DBException;
import org.iq80.leveldb.env.DbLock;
import org.iq80.leveldb.util.Closeables;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Objects;

import static java.lang.String.format;

class FileLock implements DbLock {
    private final File lockFile;
    private final FileChannel channel;
    private final java.nio.channels.FileLock lock;

    private FileLock(File lockFile, FileChannel channel, java.nio.channels.FileLock lock) {
        this.lockFile = lockFile;
        this.channel = channel;
        this.lock = lock;
    }

    @Override
    public boolean isValid() {
        return lock.isValid();
    }

    @Override
    public void release() {
        try (FileChannel closeMe = channel) {
            lock.release();
        } catch (IOException e) {
            throw new DBException(e);
        } finally {
            lockFile.delete();
        }
    }

    @Override
    public String toString() {
        return "DbLock" +
                "{lockFile=" + lockFile +
                ", lock=" + lock +
                '}';
    }

    /**
     * Attempts to acquire an exclusive lock on this file
     *
     * @param lockFile lock file
     * @return releasable db lock
     * @throws IOException If lock is already held or some other I/O error occurs
     */
    public static FileLock tryLock(File lockFile) throws IOException {
        Objects.requireNonNull(lockFile);
        // open and lock the file
        final FileChannel channel = new RandomAccessFile(lockFile, "rw").getChannel();
        try {
            java.nio.channels.FileLock lock = channel.tryLock();
            if (lock == null) {
                throw new IOException(format("Unable to acquire lock on '%s'", lockFile.getAbsolutePath()));
            }
            return new FileLock(lockFile, channel, lock);
        } catch (Exception e) {
            Closeables.closeQuietly(channel);
            throw new IOException(format("Unable to acquire lock on '%s'", lockFile.getAbsolutePath()), e);
        }
    }
}
