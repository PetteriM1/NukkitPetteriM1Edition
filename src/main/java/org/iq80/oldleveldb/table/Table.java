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
package org.iq80.oldleveldb.table;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.iq80.oldleveldb.impl.SeekingIterable;
import org.iq80.leveldb.util.Closeables;
import org.iq80.oldleveldb.util.Slice;
import org.iq80.oldleveldb.util.TableIterator;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public abstract class Table implements SeekingIterable<Slice, Slice> {
    protected final String name;
    protected final FileChannel fileChannel;
    protected final Comparator<Slice> comparator;
    protected final boolean verifyChecksums;
    protected final Block indexBlock;
    protected final BlockHandle metaindexBlockHandle;
    private final LoadingCache<BlockHandle, Block> blockCache = CacheBuilder
            .newBuilder()
            .maximumSize(1000)
            .build(CacheLoader.from(blockHandle -> {
                Block dataBlock;
                try {
                    dataBlock = readBlock(blockHandle);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return dataBlock;
            }));

    public Table(String name, FileChannel fileChannel, Comparator<Slice> comparator, boolean verifyChecksums)
            throws IOException {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(fileChannel);
        long size = fileChannel.size();
        Preconditions.checkArgument(size >= Footer.ENCODED_LENGTH);
        Preconditions.checkNotNull(comparator);

        this.name = name;
        this.fileChannel = fileChannel;
        this.verifyChecksums = verifyChecksums;
        this.comparator = comparator;

        Footer footer = init();
        indexBlock = readBlock(footer.getIndexBlockHandle());
        metaindexBlockHandle = footer.getMetaindexBlockHandle();
    }

    private static class Closer
            implements Callable<Void> {
        private final Closeable closeable;

        public Closer(Closeable closeable) {
            this.closeable = closeable;
        }

        @Override
        public Void call() {
            Closeables.closeQuietly(closeable);
            return null;
        }
    }

    public void clearBlockCache() {
        blockCache.invalidateAll();
    }

    public Callable<?> closer() {
        return new Closer(fileChannel);
    }

    protected abstract Footer init()
            throws IOException;

    @Override
    public TableIterator iterator() {
        return new TableIterator(this, indexBlock.iterator());
    }

    public Block openBlock(Slice blockEntry) {
        BlockHandle blockHandle = BlockHandle.readBlockHandle(blockEntry.input());
        try {
            return blockCache.get(blockHandle);
        } catch (ExecutionException e) {
            throw Throwables.propagate(e);
        }
    }

    protected abstract Block readBlock(BlockHandle blockHandle)
            throws IOException;

    @Override
    public String toString() {
        return "Table" +
                "{name='" + name + '\'' +
                ", comparator=" + comparator +
                ", verifyChecksums=" + verifyChecksums +
                '}';
    }
}
