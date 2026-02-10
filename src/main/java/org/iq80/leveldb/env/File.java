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

import java.util.List;

public interface File {
    boolean canRead();

    /**
     * Resolve the given path against this path.
     *
     * @param other the path to resolve against this path
     * @return the resulting path
     */
    File child(String other);

    boolean delete();

    /**
     * Delete this file and all its contained files and directories.
     *
     * @return {@code true} if all content and this file where deleted, {@code false}
     * otherwise
     */
    boolean deleteRecursively();

    boolean exists();

    String getName();

    File getParentFile();

    String getPath();

    boolean isDirectory();

    boolean isFile();

    /**
     * @return File size or {@code 0L} if file does not exist
     */
    long length();

    List<File> listFiles();

    /**
     * Creates the directory named by this file, including any
     * necessary but nonexistent parent directories.
     *
     * @return {@code true} if and only if the directory was created,
     * along with all necessary parent directories; {@code false}
     * otherwise
     */
    boolean mkdirs();

    boolean renameTo(File dest);
}
