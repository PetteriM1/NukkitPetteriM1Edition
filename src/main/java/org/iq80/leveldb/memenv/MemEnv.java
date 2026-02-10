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

import org.iq80.leveldb.Logger;
import org.iq80.leveldb.env.*;
import org.iq80.leveldb.util.Slices;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Environment that stores its data in memory
 */
public class MemEnv implements Env {
    private final MemFs fs = new MemFs();

    public static Env createEnv() {
        return new MemEnv();
    }

    @Override
    public File createTempDir(String prefix) {
        return fs.createTempDir(prefix);
    }

    @Override
    public WritableFile newAppendableFile(File file) throws IOException {
        return new MemWritableFile(fs.getOrCreateFile((MemFile) file));
    }

    @Override
    public Logger newLogger(File loggerFile) throws IOException {
        return new NoOpLogger();
    }

    @Override
    public RandomInputFile newRandomAccessFile(File file) throws IOException {
        return new MemRandomInputFile(file, fs.requireFile((MemFile) file));
    }

    @Override
    public SequentialFile newSequentialFile(File file) throws IOException {
        return new MemSequentialFile(fs.requireFile((MemFile) file));
    }

    @Override
    public WritableFile newWritableFile(File file) throws IOException {
        return new MemWritableFile(fs.getOrCreateFile((MemFile) file));
    }

    @Override
    public long nowMicros() {
        return TimeUnit.NANOSECONDS.toMicros(System.nanoTime());
    }

    @Override
    public String readFileToString(File file) throws IOException {
        byte[] read = fs.requireFile((MemFile) file).read(0, (int) file.length());
        if (read == null) {
            throw new IOException("Could not read all the data");
        }
        return new String(read, UTF_8);
    }

    @Override
    public File toFile(String filename) {
        return MemFile.createMemFile(fs, filename);
    }

    @Override
    public DbLock tryLock(File file) throws IOException {
        return fs.doLock(((MemFile) file));
    }

    @Override
    public void writeStringToFileSync(File file, String content) throws IOException {
        fs.getOrCreateFile((MemFile) file).truncate().append(Slices.wrappedBuffer(content.getBytes(UTF_8)));
    }
}
