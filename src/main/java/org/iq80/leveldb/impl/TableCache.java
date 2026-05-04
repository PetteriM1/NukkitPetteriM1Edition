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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import org.iq80.leveldb.DBException;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.env.Env;
import org.iq80.leveldb.env.File;
import org.iq80.leveldb.env.RandomInputFile;
import org.iq80.leveldb.iterator.InternalTableIterator;
import org.iq80.leveldb.table.*;
import org.iq80.leveldb.util.Closeables;
import org.iq80.leveldb.util.ILRUCache;
import org.iq80.leveldb.util.LRUCache;
import org.iq80.leveldb.util.Slice;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class TableCache {
    private final LoadingCache<Long, TableAndFile> cache;
    private final ILRUCache<CacheKey, Slice> blockCache;

    public TableCache(final File databaseDir,
                      int tableCacheSize,
                      final UserComparator userComparator,
                      final Options options, Env env) {
        Objects.requireNonNull(databaseDir);
        blockCache = options.cacheSize() == 0 ? null : LRUCache.createCache((int) options.cacheSize(), new BlockHandleSliceWeigher());
        cache = CacheBuilder.newBuilder()
                .maximumSize(tableCacheSize)
                .removalListener((RemovalListener<Long, TableAndFile>) notification -> {
                    final TableAndFile value = notification.getValue();
                    if (value != null) {
                        final Table table = value.getTable();
                        try {
                            //end user is required to close resources/iterators
                            //no need to rely on GC to collect files even for MM Files.
                            table.close();
                        } catch (IOException e) {
                            throw new DBException(e);
                        }
                    }
                })
                .build(new CacheLoader<Long, TableAndFile>() {
                    @Override
                    public TableAndFile load(Long fileNumber)
                            throws IOException {
                        return new TableAndFile(databaseDir, fileNumber, userComparator, options, blockCache, env);
                    }
                });
    }

    private static final class TableAndFile {
        private final Table table;

        private TableAndFile(File databaseDir, long fileNumber, UserComparator userComparator, Options options, ILRUCache<CacheKey, Slice> blockCache, Env env)
                throws IOException {
            final File tableFile = tableFileName(databaseDir, fileNumber);
            RandomInputFile source = env.newRandomAccessFile(tableFile);
            table = Closeables.wrapResource(() -> {
                final FilterPolicy filterPolicy = (FilterPolicy) options.filterPolicy();
                return new Table(source, userComparator,
                        options.paranoidChecks(), blockCache, filterPolicy);
            }, source);
        }

        public Table getTable() {
            return table;
        }

        private File tableFileName(File databaseDir, long fileNumber) {
            final String tableFileName = Filename.tableFileName(fileNumber);
            File tableFile = databaseDir.child(tableFileName);
            if (!tableFile.canRead()) {
                // attempt to open older .sst extension
                final String sstFileName = Filename.sstTableFileName(fileNumber);
                final File sstPath = databaseDir.child(sstFileName);
                if (sstPath.canRead()) {
                    tableFile = sstPath;
                }
            }
            return tableFile;
        }
    }

    public long getApproximateMemoryUsage() {
        return blockCache.getApproximateMemoryUsage();
    }

    public void close() {
        invalidateAll();
    }

    public void evict(long number) {
        cache.invalidate(number);
    }

    public <T> T get(ReadOptions options, Slice key, FileMetaData fileMetaData, KeyValueFunction<T> resultBuilder) {
        try (Table table = getTable(fileMetaData.getNumber())) { //same as release
            return table.internalGet(options, key, resultBuilder);
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    public long getApproximateOffsetOf(FileMetaData file, Slice key) {
        try (Table table = getTable(file.getNumber())) {
            return table.getApproximateOffsetOf(key);
        } catch (IOException e) {
            throw new DBException(e);
        }
    }

    private Table getTable(long number) {
        Table table;
        try {
            do {
                table = cache.get(number).getTable();
            } while (!table.retain());
        } catch (ExecutionException e) {
            Throwable cause = e;
            if (e.getCause() != null) {
                cause = e.getCause();
            }
            throw new DBException("Could not open table " + number, cause);
        }
        return table;
    }

    /**
     * Discards all entries in table and block (if any).
     */
    public void invalidateAll() {
        if (blockCache != null) {
            blockCache.invalidateAll();
        }
        cache.invalidateAll();
    }

    public InternalTableIterator newIterator(FileMetaData file, ReadOptions options) throws IOException {
        return newIterator(file.getNumber(), options);
    }

    public InternalTableIterator newIterator(long number, ReadOptions options) throws IOException {
        try (Table table = getTable(number)) { //same as release
            return new InternalTableIterator(table.iterator(options)); //make its own retain
        }
    }
}
