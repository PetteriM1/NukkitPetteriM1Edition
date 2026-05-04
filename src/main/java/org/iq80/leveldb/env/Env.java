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
package org.iq80.leveldb.env;

import org.iq80.leveldb.Logger;

import java.io.IOException;

public interface Env {
    /**
     * Create a temporary directory in filesystem
     *
     * @param prefix prefix to use as name
     * @return newly created directory
     */
    File createTempDir(String prefix);

    /**
     * Create an WritableFile that either appends to an existing file, or
     * writes to a new file (if the file does not exist to begin with).
     * <p>
     * May return an IsNotSupportedError error if this Env does
     * not allow appending to an existing file.  Users of Env (including
     * the leveldb implementation) must be prepared to deal with
     * an Env that does not support appending. TODO
     *
     * @return new or existing writable file only accessible by one thread at a time.
     * @throws IOException If the file is inaccessible.
     */
    WritableFile newAppendableFile(File file) throws IOException;

    /**
     * Create and return a log file for storing informational messages.
     *
     * @param loggerFile logger file
     * @return logger instance if file is writable, {@code null} otherwise
     */
    Logger newLogger(File loggerFile) throws IOException;

    /**
     * Create a brand new random access read-only file with the
     * specified file name.
     *
     * @return new file that may be concurrently accessed by multiple threads.
     * @throws IOException If the file does not exist or inaccessible.
     */
    RandomInputFile newRandomAccessFile(File file) throws IOException;

    /**
     * Create a brand new sequentially-readable file with the specified file name.
     *
     * @return new file that can only be accessed by one thread at a time.
     * @throws IOException If the file does not exist or inaccessible.
     */
    SequentialFile newSequentialFile(File file) throws IOException;

    /**
     * Create an object that writes to a new file with the specified
     * name.  Deletes any existing file with the same name and creates a
     * new file.
     * <p>
     *
     * @return new file that can be accessed by one thread at a time.
     * @throws IOException If the file not writable.
     */
    WritableFile newWritableFile(File file) throws IOException;

    long nowMicros();

    /**
     * Read full file content to string
     *
     * @param file file location
     * @throws IOException If the file not readable.
     */
    String readFileToString(File file) throws IOException;

    /**
     * Transform a file name into a {@link File} instance scoped to this {@link Env} instance
     *
     * @param filename full file name
     * @return File instance
     */
    File toFile(String filename);

    /**
     * Attempts to acquire an exclusive lock on lock file
     *
     * @param lockFile lock file
     * @return releasable db lock
     * @throws IOException If lock is already held or some other I/O error occurs
     */
    DbLock tryLock(File lockFile) throws IOException;

    /**
     * Write {@code content} to file. Replace existing content.
     *
     * @param file    file location
     * @param content new content
     * @throws IOException If the file not writable.
     */
    void writeStringToFileSync(File file, String content) throws IOException;
}
