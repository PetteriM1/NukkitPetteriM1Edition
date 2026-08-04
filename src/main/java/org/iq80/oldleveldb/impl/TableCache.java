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
package org.iq80.oldleveldb.impl;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import org.iq80.oldleveldb.table.FileChannelTable;
import org.iq80.oldleveldb.table.MMapTable;
import org.iq80.oldleveldb.table.Table;
import org.iq80.oldleveldb.table.UserComparator;
import org.iq80.leveldb.util.Closeables;
import org.iq80.oldleveldb.util.Finalizer;
import org.iq80.oldleveldb.util.InternalTableIterator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TableCache {
    private final LoadingCache<Long, TableAndFile> cache;
    private final Finalizer<Table> finalizer = new Finalizer<>(1);

    public TableCache(final File databaseDir, int tableCacheSize, final UserComparator userComparator, final boolean verifyChecksums) {
        Preconditions.checkNotNull(databaseDir);

        cache = CacheBuilder.newBuilder()
                .maximumSize(tableCacheSize)
                .removalListener((RemovalListener<Long, TableAndFile>) notification -> {
                    Table table = notification.getValue().getTable();
                    finalizer.addCleanup(table, table.closer());
                })
                .build(CacheLoader.from(fileNumber -> {
                    try {
                        return new TableAndFile(databaseDir, fileNumber, userComparator, verifyChecksums);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
    }

    public InternalTableIterator newIterator(FileMetaData file) {
        return newIterator(file.getNumber());
    }

    public InternalTableIterator newIterator(long number) {
        return new InternalTableIterator(getTable(number).iterator());
    }

    private Table getTable(long number) {
        Table table;
        try {
            table = cache.get(number).getTable();
        } catch (ExecutionException e) {
            Throwable cause = e;
            if (e.getCause() != null) {
                cause = e.getCause();
            }
            throw new RuntimeException("Could not open table " + number, cause);
        }
        return table;
    }

    public void close() {
        cache.invalidateAll();
        finalizer.destroy();
    }

    public void evict(long number) {
        cache.invalidate(number);
    }

    public void clearBlockCaches() {
        for (Map.Entry<Long, TableAndFile> longTableAndFileEntry : cache.asMap().entrySet()) {
            longTableAndFileEntry.getValue().getTable().clearBlockCache();
        }
    }

    private static final class TableAndFile {
        private final Table table;

        private TableAndFile(File databaseDir, long fileNumber, UserComparator userComparator, boolean verifyChecksums)
                throws IOException {
            String tableFileName = Filename.tableFileName(fileNumber);
            File tableFile = new File(databaseDir, tableFileName);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(tableFile);
                FileChannel fileChannel = fis.getChannel();
                if (Iq80DBFactory.USE_MMAP) {
                    table = new MMapTable(tableFile.getAbsolutePath(), fileChannel, userComparator, verifyChecksums);
                    // We can close the channel and input stream as the mapping does not need them
                    Closeables.closeQuietly(fis);
                } else {
                    table = new FileChannelTable(tableFile.getAbsolutePath(), fileChannel, userComparator, verifyChecksums);
                }
            } catch (IOException ioe) {
                Closeables.closeQuietly(fis);
                throw ioe;
            }
        }

        public Table getTable() {
            return table;
        }
    }
}
