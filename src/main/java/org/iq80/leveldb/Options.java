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
package org.iq80.leveldb;

import java.util.Objects;

/**
 * Options to control the behavior of a database
 */
public class Options {
    private boolean createIfMissing = true;
    private boolean errorIfExists;
    private int writeBufferSize = 4 << 20;

    private int maxOpenFiles = 1000;

    private int blockRestartInterval = 16;
    private int maxFileSize = 2 << 20;
    private int blockSize = 4 * 1024;
    private CompressionType compressionType = CompressionType.ZLIB_RAW;
    private boolean paranoidChecks;
    private DBComparator comparator;
    private Logger logger;
    private long cacheSize = 8 << 20;
    private XFilterPolicy filterPolicy;
    private boolean reuseLogs = false;

    /**
     * Clone, create a copy of the provided instance of {@link Options}
     */
    public static Options fromOptions(Options options) {
        Objects.requireNonNull(options);
        final Options options1 = new Options();
        options1.createIfMissing = options.createIfMissing;
        options1.errorIfExists = options.errorIfExists;
        options1.writeBufferSize = options.writeBufferSize;
        options1.maxOpenFiles = options.maxOpenFiles;
        options1.blockRestartInterval = options.blockRestartInterval;
        options1.maxFileSize = options.maxFileSize;
        options1.blockSize = options.blockSize;
        options1.compressionType = options.compressionType;
        options1.paranoidChecks = options.paranoidChecks;
        options1.comparator = options.comparator;
        options1.logger = options.logger;
        options1.cacheSize = options.cacheSize;
        options1.filterPolicy = options.filterPolicy;
        options1.reuseLogs = options.reuseLogs;
        return options1;
    }

    /**
     * Create an Options object with default values for all fields.
     */
    public static Options newDefaultOptions() {
        return new Options();
    }

    /**
     * If true, the database will be created if it is missing.
     */
    public boolean createIfMissing() {
        return createIfMissing;
    }

    public Options createIfMissing(boolean createIfMissing) {
        this.createIfMissing = createIfMissing;
        return this;
    }

    public boolean errorIfExists() {
        return errorIfExists;
    }

    /**
     * If true, an error is raised if the database already exists.
     */
    public Options errorIfExists(boolean errorIfExists) {
        this.errorIfExists = errorIfExists;
        return this;
    }

    public int writeBufferSize() {
        return writeBufferSize;
    }

    /**
     * Parameters that affect performance
     * <p>
     * Amount of data to build up in memory (backed by an unsorted log
     * on disk) before converting to a sorted on-disk file.
     * <p>
     * Larger values increase performance, especially during bulk loads.
     * Up to two write buffers may be held in memory at the same time,
     * so you may wish to adjust this parameter to control memory usage.
     * Also, a larger write buffer will result in a longer recovery time
     * the next time the database is opened.
     */
    public Options writeBufferSize(int writeBufferSize) {
        this.writeBufferSize = writeBufferSize;
        return this;
    }

    public int maxOpenFiles() {
        return maxOpenFiles;
    }

    /**
     * Number of open files that can be used by the DB.  You may need to
     * increase this if your database has a large working set (budget
     * one open file per 2MB of working set).
     */
    public Options maxOpenFiles(int maxOpenFiles) {
        this.maxOpenFiles = maxOpenFiles;
        return this;
    }

    public int blockRestartInterval() {
        return blockRestartInterval;
    }

    /**
     * Number of keys between restart points for delta encoding of keys.
     * This parameter can be changed dynamically.  Most clients should
     * leave this parameter alone.
     */
    public Options blockRestartInterval(int blockRestartInterval) {
        this.blockRestartInterval = blockRestartInterval;
        return this;
    }

    public int maxFileSize() {
        return maxFileSize;
    }

    /**
     * Leveldb will write up to this amount of bytes to a file before
     * switching to a new one.
     * Most clients should leave this parameter alone.  However if your
     * filesystem is more efficient with larger files, you could
     * consider increasing the value.  The downside will be longer
     * compactions and hence longer latency/performance hiccups.
     * Another reason to increase this parameter might be when you are
     * initially populating a large database.
     * <p>
     * Default: 2MB
     *
     * @param maxFileSize max file size int bytes
     */
    public Options maxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
        return this;
    }

    public int blockSize() {
        return blockSize;
    }

    /**
     * Approximate size of user data packed per block.  Note that the
     * block size specified here corresponds to uncompressed data.  The
     * actual size of the unit read from disk may be smaller if
     * compression is enabled.  This parameter can be changed dynamically.
     */
    public Options blockSize(int blockSize) {
        this.blockSize = blockSize;
        return this;
    }

    public CompressionType compressionType() {
        return compressionType;
    }

    /**
     * Compress blocks using the specified compression algorithm.  This
     * parameter can be changed dynamically.
     * <p>
     * Default: {@link CompressionType#ZLIB}, which is used for Mojangs LevelDB fork.
     * <p>
     * Snappy is also possible, if Snappy compression is not available, {@link CompressionType#NONE}
     * will be used.
     */
    public Options compressionType(CompressionType compressionType) {
        Objects.requireNonNull(compressionType);
        this.compressionType = compressionType;
        return this;
    }

    public long cacheSize() {
        return cacheSize;
    }

    /**
     * If {@code cacheSize} is zero, no block cache will be used-
     * If non-zero, use the specified cache size for blocks.
     * By default leveldb will automatically create and use an 8MB internal cache.
     */
    public Options cacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
        return this;
    }

    public DBComparator comparator() {
        return comparator;
    }

    /**
     * Parameters that affect behavior
     * <p>
     * Comparator used to define the order of keys in the table.
     * Default: a comparator that uses lexicographic byte-wise ordering
     * <p>
     * REQUIRES: The client must ensure that the comparator supplied
     * here has the same name and orders keys *exactly* the same as the
     * comparator provided to previous open calls on the same DB.
     */
    public Options comparator(DBComparator comparator) {
        this.comparator = comparator;
        return this;
    }

    public Logger logger() {
        return logger;
    }

    /**
     * Any internal progress/error information generated by the db will
     * be written to {@code logger} if it is non-null, or to a file stored
     * in the same directory as the DB contents if info_log is null.
     */
    public Options logger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public boolean paranoidChecks() {
        return paranoidChecks;
    }

    /**
     * If true, the implementation will do aggressive checking of the
     * data it is processing and will stop early if it detects any
     * errors.  This may have unforeseen ramifications: for example, a
     * corruption of one DB entry may cause a large number of entries to
     * become unreadable or for the entire DB to become unopenable.
     */
    public Options paranoidChecks(boolean paranoidChecks) {
        this.paranoidChecks = paranoidChecks;
        return this;
    }

    /**
     * If non-null, use the specified filter policy to reduce disk reads.
     * Many applications will benefit from passing an instance of BloomFilter
     *
     * @param filterPolicy new filter policy
     * @return self
     */
    public Options filterPolicy(XFilterPolicy filterPolicy) {
        this.filterPolicy = filterPolicy;
        return this;
    }

    public XFilterPolicy filterPolicy() {
        return filterPolicy;
    }

    /**
     * If true, append to existing MANIFEST and log files
     * when a database is opened.  This can significantly speed up open.
     */
    public Options reuseLogs(boolean reuseLogs) {
        this.reuseLogs = reuseLogs;
        return this;
    }

    public boolean reuseLogs() {
        return this.reuseLogs;
    }
}
