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

import com.google.common.io.Files;
import org.iq80.leveldb.Logger;
import org.iq80.leveldb.env.*;
import org.iq80.leveldb.util.Slice;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

public class EnvImpl implements Env {
    private static final int PAGE_SIZE = 1024 * 1024;
    private final MmapLimiter mmapLimiter;

    private EnvImpl(MmapLimiter mmapLimiter) {
        this.mmapLimiter = mmapLimiter;
    }

    private static class DelegateRandomInputFile implements RandomInputFile {
        private final MmapLimiter mmapLimiter;
        private final RandomInputFile open;

        DelegateRandomInputFile(MmapLimiter mmapLimiter, RandomInputFile open) {
            this.mmapLimiter = mmapLimiter;
            this.open = open;
        }

        @Override
        public void close() throws IOException {
            try {
                open.close();
            } finally {
                mmapLimiter.release();
            }
        }

        @Override
        public ByteBuffer read(long offset, int length) throws IOException {
            return open.read(offset, length);
        }

        @Override
        public long size() {
            return open.size();
        }
    }

    private static class DelegateWritableFile implements WritableFile {
        private final MmapLimiter mmapLimiter;
        private final WritableFile open;

        DelegateWritableFile(MmapLimiter mmapLimiter, WritableFile open) {
            this.mmapLimiter = mmapLimiter;
            this.open = open;
        }

        @Override
        public void append(Slice data) throws IOException {
            open.append(data);
        }

        @Override
        public void close() throws IOException {
            try {
                open.close();
            } finally {
                mmapLimiter.release();
            }
        }

        @Override
        public void force() throws IOException {
            open.force();
        }
    }

    public static Env createEnv() {
        return new EnvImpl(MmapLimiter.defaultLimiter());
    }

    public static Env createEnv(MmapLimiter mmapLimiter) {
        return new EnvImpl(mmapLimiter);
    }

    @Override
    public File createTempDir(String prefix) {
        return JavaFile.fromFile(FileUtils.createTempDir(prefix));
    }

    @Override
    public WritableFile newAppendableFile(File file) throws IOException {
        return UnbufferedWritableFile.open(JavaFile.toFile(file), true);
    }

    @Override
    public Logger newLogger(File loggerFile) throws IOException {
        return new NoOpLogger(); //different that native but avoid for ever growing log file
    }

    @Override
    public RandomInputFile newRandomAccessFile(File file) throws IOException {
        if (mmapLimiter.acquire()) {
            try {
                return new DelegateRandomInputFile(mmapLimiter, MMRandomInputFile.open(JavaFile.toFile(file)));
            } catch (IOException e) {
                mmapLimiter.release();
                throw e;
            }
        }
        return UnbufferedRandomInputFile.open(JavaFile.toFile(file));
    }

    @Override
    public SequentialFile newSequentialFile(File file) throws IOException {
        return SequentialFileImpl.open(JavaFile.toFile(file));
    }

    @Override
    public WritableFile newWritableFile(File file) throws IOException {
        if (mmapLimiter.acquire()) {
            try {
                return new DelegateWritableFile(mmapLimiter, MMWritableFile.open(JavaFile.toFile(file), PAGE_SIZE));
            } catch (IOException e) {
                mmapLimiter.release();
                throw e;
            }
        }
        return UnbufferedWritableFile.open(JavaFile.toFile(file), false);
    }

    @Override
    public long nowMicros() {
        return TimeUnit.NANOSECONDS.toMicros(System.nanoTime());
    }

    @Override
    public String readFileToString(File file) throws IOException {
        return Files.asCharSource(JavaFile.toFile(file), UTF_8).read();
    }

    @Override
    public File toFile(String filename) {
        return JavaFile.fromFile(new java.io.File(filename));
    }

    /**
     * Attempts to acquire an exclusive lock on this file
     *
     * @param file lock file
     * @return releasable db lock
     * @throws IOException If lock is already held or some other I/O error occurs
     */
    @Override
    public DbLock tryLock(File file) throws IOException {
        return FileLock.tryLock(JavaFile.toFile(file));
    }

    @Override
    public void writeStringToFileSync(File file, String content) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(JavaFile.toFile(file))) {
            stream.write(content.getBytes(UTF_8));
            stream.flush();
            stream.getFD().sync();
        }
    }
}
