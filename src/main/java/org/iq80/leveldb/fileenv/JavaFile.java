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

import org.iq80.leveldb.env.File;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaFile implements File {
    private final java.io.File file;

    private JavaFile(java.io.File file) {
        this.file = file;
    }

    public static JavaFile fromFile(java.io.File path) {
        return new JavaFile(path);
    }

    static java.io.File toFile(File file) {
        return ((JavaFile) file).file;
    }

    @Override
    public File child(String name) {
        return new JavaFile(new java.io.File(file, name));
    }

    @Override
    public boolean mkdirs() {
        return file.mkdirs();
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public File getParentFile() {
        return new JavaFile(file.getParentFile());
    }

    @Override
    public String getPath() {
        return file.getAbsolutePath();
    }

    @Override
    public boolean canRead() {
        return file.canRead();
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    @Override
    public boolean isFile() {
        return file.isFile();
    }

    @Override
    public long length() {
        return file.length();
    }

    @Override
    public boolean delete() {
        return file.delete();
    }

    @Override
    public List<File> listFiles() {
        java.io.File[] values = file.listFiles();
        return values == null ? Collections.emptyList() : Stream.of(values).map(JavaFile::new).collect(Collectors.toList());
    }

    @Override
    public boolean renameTo(File dest) {
        return file.renameTo(((JavaFile) dest).file);
    }

    @Override
    public boolean deleteRecursively() {
        return FileUtils.deleteRecursively(file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JavaFile javaFile = (JavaFile) o;
        return Objects.equals(file, javaFile.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }

    @Override
    public String toString() {
        return String.valueOf(file);
    }
}
