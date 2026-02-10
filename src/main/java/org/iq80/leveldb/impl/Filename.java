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
package org.iq80.leveldb.impl;

import com.google.common.base.Strings;
import org.iq80.leveldb.env.Env;
import org.iq80.leveldb.env.File;

import java.io.IOException;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.Long.parseUnsignedLong;

public final class Filename {
    private Filename() {
    }

    public enum FileType {
        LOG,
        DB_LOCK,
        TABLE,
        DESCRIPTOR,
        CURRENT,
        TEMP,
        INFO_LOG  // Either the current one, or an old one
    }

    public static class FileInfo {
        private final FileType fileType;
        private final long fileNumber;

        public FileInfo(FileType fileType) {
            this(fileType, 0);
        }

        public FileInfo(FileType fileType, long fileNumber) {
            Objects.requireNonNull(fileType);
            this.fileType = fileType;
            this.fileNumber = fileNumber;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            FileInfo fileInfo = (FileInfo) o;

            if (fileNumber != fileInfo.fileNumber) {
                return false;
            }
            return fileType == fileInfo.fileType;
        }

        public long getFileNumber() {
            return fileNumber;
        }

        public FileType getFileType() {
            return fileType;
        }

        @Override
        public int hashCode() {
            int result = fileType.hashCode();
            result = 31 * result + (int) (fileNumber ^ (fileNumber >>> 32));
            return result;
        }

        @Override
        public String toString() {
            return "FileInfo" +
                    "{fileType=" + fileType +
                    ", fileNumber=" + Long.toUnsignedString(fileNumber) +
                    '}';
        }
    }

    /**
     * Return the name of the current file.
     */
    public static String currentFileName() {
        return "CURRENT";
    }

    /**
     * Return the name of the descriptor file with the specified incarnation number.
     */
    public static String descriptorFileName(long number) {
        checkArgument(number >= 0, "number is negative");
        return String.format("MANIFEST-%06d", number);
    }

    /**
     * Read "CURRENT" file, which contains a pointer to the current manifest file
     *
     * @param databaseDir DB base directory
     * @param env         system environment
     * @return current manifest file
     * @throws IOException           on any IO exception
     * @throws IllegalStateException if file does not exist or invalid file content
     */
    public static String getCurrentFile(File databaseDir, Env env)
            throws IOException {
        // Read "CURRENT" file, which contains a pointer to the current manifest file
        File currentFile = databaseDir.child(currentFileName());
        checkState(currentFile.exists(), "CURRENT file does not exist");

        String descriptorName = env.readFileToString(currentFile);
        if (descriptorName.isEmpty() || descriptorName.charAt(descriptorName.length() - 1) != '\n') {
            throw new IllegalStateException("CURRENT file does not end with newline");
        }
        return descriptorName.substring(0, descriptorName.length() - 1);
    }

    /**
     * Return the name of the info log file.
     */
    public static String infoLogFileName() {
        return "LOG";
    }

    /**
     * Return the name of the lock file.
     */
    public static String lockFileName() {
        return "LOCK";
    }

    /**
     * Return the name of the log file with the specified number.
     */
    public static String logFileName(long number) {
        return makeFileName(number, "log");
    }

    /**
     * Make a new file name
     *
     * @param number unsigned number
     * @param suffix file name suffix
     * @return new file name.
     */
    private static String makeFileName(long number, String suffix) {
        Objects.requireNonNull(suffix);
        return String.format("%s.%s", Strings.padStart(Long.toUnsignedString(number), 6, '0'), suffix);
    }

    /**
     * Return the name of the old info log file.
     */
    public static String oldInfoLogFileName() {
        return "LOG.old";
    }

    /**
     * If filename is a leveldb file, store the type of the file in *type.
     * The number encoded in the filename is stored in *number.  If the
     * filename was successfully parsed, returns true.  Else return false.
     */
    public static FileInfo parseFileName(File file) {
        // Owned filenames have the form:
        //    dbname/CURRENT
        //    dbname/LOCK
        //    dbname/LOG
        //    dbname/LOG.old
        //    dbname/MANIFEST-[0-9]+
        //    dbname/[0-9]+.(log|sst|dbtmp)
        try {
            String fileName = file.getName();
            if ("CURRENT".equals(fileName)) {
                return new FileInfo(FileType.CURRENT);
            } else if ("LOCK".equals(fileName)) {
                return new FileInfo(FileType.DB_LOCK);
            } else if ("LOG".equals(fileName) || "LOG.old".equals(fileName)) {
                return new FileInfo(FileType.INFO_LOG);
            } else if (fileName.startsWith("MANIFEST-")) {
                long fileNumber = parseLong(removePrefix(fileName, "MANIFEST-"));
                return new FileInfo(FileType.DESCRIPTOR, fileNumber);
            } else if (fileName.endsWith(".log")) {
                long fileNumber = parseLong(removeSuffix(fileName, ".log"));
                return new FileInfo(FileType.LOG, fileNumber);
            } else if (fileName.endsWith(".sst") || fileName.endsWith(".ldb")) {
                long fileNumber = parseLong(fileName.substring(0, fileName.lastIndexOf('.')));
                return new FileInfo(FileType.TABLE, fileNumber);
            } else if (fileName.endsWith(".dbtmp")) {
                long fileNumber = parseLong(removeSuffix(fileName, ".dbtmp"));
                return new FileInfo(FileType.TEMP, fileNumber);
            }
        } catch (Exception ignore) {
            //filename is incorrect/not supported
        }
        return null;
    }

    /**
     * Parse unsigned long string
     */
    private static long parseLong(String str) {
        return parseUnsignedLong(str, 10);
    }

    private static String removePrefix(String value, String prefix) {
        return value.substring(prefix.length());
    }

    private static String removeSuffix(String value, String suffix) {
        return value.substring(0, value.length() - suffix.length());
    }

    /**
     * Make the CURRENT file point to the descriptor file with the
     * specified number.
     *
     * @throws IOException              on any IO exception
     * @throws IllegalArgumentException on invalid descriptorNumber
     */
    public static void setCurrentFile(File databaseDir, long descriptorNumber, Env env)
            throws IOException {
        String manifest = descriptorFileName(descriptorNumber);
        String temp = tempFileName(descriptorNumber);

        File tempFile = databaseDir.child(temp);
        env.writeStringToFileSync(tempFile, manifest + "\n");

        File to = databaseDir.child(currentFileName());
        boolean ok = tempFile.renameTo(to);
        if (!ok) {
            tempFile.delete();
            env.writeStringToFileSync(to, manifest + "\n");
        }
    }

    /**
     * Return the deprecated name of the sstable with the specified number.
     */
    public static String sstTableFileName(long number) {
        return makeFileName(number, "sst");
    }

    /**
     * Return the name of the sstable with the specified number.
     */
    public static String tableFileName(long number) {
        return makeFileName(number, "ldb");
    }

    /**
     * Return the name of a temporary file with the specified number.
     */
    public static String tempFileName(long number) {
        return makeFileName(number, "dbtmp");
    }
}
