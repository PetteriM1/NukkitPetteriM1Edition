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
package org.iq80.leveldb.table;

import cn.nukkit.utils.SnappyCompression;
import cn.nukkit.utils.Zlib;
import com.google.common.base.Throwables;
import org.iq80.leveldb.DBException;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.env.RandomInputFile;
import org.iq80.leveldb.iterator.SeekingIterators;
import org.iq80.leveldb.iterator.SliceIterator;
import org.iq80.leveldb.util.*;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.iq80.leveldb.CompressionType.*;

public final class Table
        implements Closeable {
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final AtomicLong ID_GENERATOR = new AtomicLong();
    private final long id = ID_GENERATOR.incrementAndGet();
    private final Comparator<Slice> comparator;
    private final Block indexBlock;
    private final BlockHandle metaindexBlockHandle;
    private final RandomInputFile source;
    private final ILRUCache<CacheKey, Slice> blockCache;
    private final FilterBlockReader filter;
    //use ref count to release resource early
    //external user iterator are required to be closed
    private final AtomicInteger refCount = new AtomicInteger(1);

    public Table(RandomInputFile source, Comparator<Slice> comparator, boolean paranoidChecks, ILRUCache<CacheKey, Slice> blockCache, final FilterPolicy filterPolicy)
            throws IOException {
        this.source = source;
        this.blockCache = blockCache;
        Objects.requireNonNull(source);
        long size = source.size();
        checkArgument(size >= Footer.ENCODED_LENGTH, "File is corrupt: size must be at least %s bytes", Footer.ENCODED_LENGTH);
        Objects.requireNonNull(comparator);

        this.comparator = comparator;
        final ByteBuffer footerData = source.read(size - Footer.ENCODED_LENGTH, Footer.ENCODED_LENGTH);

        Footer footer = Footer.readFooter(Slices.avoidCopiedBuffer(footerData));
        indexBlock = new Block(readRawBlock(footer.getIndexBlockHandle(), paranoidChecks), comparator); //no need for cache
        metaindexBlockHandle = footer.getMetaindexBlockHandle();
        this.filter = readMeta(filterPolicy, paranoidChecks);

    }

    private FilterBlockReader readMeta(FilterPolicy filterPolicy, boolean verifyChecksum) throws IOException {
        assert refCount.get() > 0;
        if (filterPolicy == null) {
            return null;  // Do not need any metadata
        }

        final Block meta = new Block(readRawBlock(metaindexBlockHandle, verifyChecksum), new BytewiseComparator());
        try (BlockIterator iterator = meta.iterator()) {
            final Slice targetKey = new Slice(("filter." + filterPolicy.name()).getBytes(CHARSET));
            if (iterator.seek(targetKey) && iterator.key().equals(targetKey)) {
                return readFilter(filterPolicy, iterator.value(), verifyChecksum);
            } else {
                return null;
            }
        }
    }

    private FilterBlockReader readFilter(FilterPolicy filterPolicy, Slice filterHandle, boolean verifyChecksum) throws IOException {
        assert refCount.get() > 0;
        final Slice filterBlock = readRawBlock(BlockHandle.readBlockHandle(filterHandle.input()), verifyChecksum);
        return new FilterBlockReader(filterPolicy, filterBlock);
    }

    public SliceIterator iterator(ReadOptions options) {
        assert refCount.get() > 0;
        this.retain();
        return SeekingIterators.twoLevelSliceIterator(indexBlock.iterator(), blockHandle -> openBlock(options, blockHandle), this::release);
    }

    private BlockIterator openBlock(ReadOptions options, Slice blockHandle) {
        Block dataBlock = openBlock(blockHandle, options);
        return dataBlock.iterator();
    }

    public FilterBlockReader getFilter() {
        assert refCount.get() > 0;
        return filter;
    }

    public Block openBlock(Slice blockEntry, ReadOptions options) {
        assert refCount.get() > 0;
        BlockHandle blockHandle = BlockHandle.readBlockHandle(blockEntry.input());
        Block dataBlock;
        try {
            dataBlock = readBlock(blockHandle, options);
        } catch (IOException e) {
            throw new DBException(e);
        }
        return dataBlock;
    }

    private Block readBlock(BlockHandle blockHandle, ReadOptions options)
            throws IOException {
        assert refCount.get() > 0;
        try {
            final Slice rawBlock;
            if (blockCache == null) {
                rawBlock = readRawBlock(blockHandle, options.verifyChecksums());
            } else if (!options.fillCache()) {
                Slice ifPresent = blockCache.getIfPresent(new CacheKey(id, blockHandle));
                if (ifPresent == null) {
                    rawBlock = readRawBlock(blockHandle, options.verifyChecksums());
                } else {
                    rawBlock = ifPresent;
                }
            } else {
                rawBlock = blockCache.load(new CacheKey(id, blockHandle), () -> readRawBlock(blockHandle, options.verifyChecksums()));
            }
            return new Block(rawBlock, comparator);
        } catch (ExecutionException e) {
            Throwables.propagateIfPossible(e.getCause(), IOException.class);
            throw new IOException(e.getCause());
        }
    }

    private Slice readRawBlock(BlockHandle blockHandle, boolean verifyChecksum)
            throws IOException {
        assert refCount.get() > 0;
        // read block trailer
        final ByteBuffer content = source.read(blockHandle.getOffset(), blockHandle.getFullBlockSize());
        int limit = content.limit();
        int position = content.position();
        int trailerStart = position + blockHandle.getDataSize();
        content.position(trailerStart);
        final BlockTrailer blockTrailer = BlockTrailer.readBlockTrailer(Slices.avoidCopiedBuffer(content));

        // only verify check sums if explicitly asked by the user
        if (verifyChecksum) {
            // checksum data and the compression type in the trailer
            PureJavaCrc32C checksum = new PureJavaCrc32C();
            content.position(position).limit(trailerStart /*content*/ + 1/*type*/);
            checksum.update(content);
            int actualCrc32c = checksum.getMaskedValue();

            checkState(blockTrailer.getCrc32c() == actualCrc32c, "Block corrupted: checksum mismatch");
        }

        // decompress data
        Slice uncompressedData;
        content.position(position);
        content.limit(limit - BlockTrailer.ENCODED_LENGTH);

        byte[] bytes = new byte[content.remaining()];
        content.get(bytes);

        if (blockTrailer.getCompressionType() == ZLIB_RAW) {
            uncompressedData = Slices.wrappedBuffer(Zlib.inflateRaw(bytes, 0));
        } else if (blockTrailer.getCompressionType() == ZLIB) {
            uncompressedData = Slices.wrappedBuffer(Zlib.inflate(bytes, 0));
        } else if (blockTrailer.getCompressionType() == SNAPPY) {
            uncompressedData = Slices.wrappedBuffer(SnappyCompression.decompress(bytes, 0));
        } else {
            uncompressedData = Slices.wrappedBuffer(bytes);
        }

        return uncompressedData;
    }

    public <T> T internalGet(ReadOptions options, Slice key, KeyValueFunction<T> keyValueFunction) {
        assert refCount.get() > 0;
        try (final BlockIterator iterator = indexBlock.iterator()) {
            if (iterator.seek(key)) {
                final Slice handleValue = iterator.value();
                if (filter != null && !filter.keyMayMatch(BlockHandle.readBlockHandle(handleValue.input()).getOffset(), key)) {
                    return null;
                } else {
                    try (BlockIterator iterator1 = openBlock(handleValue, options).iterator()) {
                        if (iterator1.seek(key)) {
                            return keyValueFunction.apply(iterator1.key(), iterator1.value());
                        }
                    }
                }
            }
            return null;
        }
    }

    /**
     * Given a key, return an approximate byte offset in the file where
     * the data for that key begins (or would begin if the key were
     * present in the file).  The returned value is in terms of file
     * bytes, and so includes effects like compression of the underlying data.
     * For example, the approximate offset of the last key in the table will
     * be close to the file length.
     */
    public long getApproximateOffsetOf(Slice key) {
        assert refCount.get() > 0;
        try (BlockIterator iterator = indexBlock.iterator()) {
            if (iterator.seek(key)) {
                BlockHandle blockHandle = BlockHandle.readBlockHandle(iterator.value().input());
                return blockHandle.getOffset();
            }
        }

        // key is past the last key in the file.  Approximate the offset
        // by returning the offset of the metaindex block (which is
        // right near the end of the file).
        return metaindexBlockHandle.getOffset();
    }

    /**
     * Try to retain current instance.
     *
     * @return {@code true} if table was successfully retained, {@code false} otherwise
     */
    public boolean retain() {
        int refs;
        do {
            refs = refCount.get();
            if (refs == 0) {
                return false; //already released. do not use!
            }
        } while (!refCount.compareAndSet(refs, refs + 1));
        return true;
    }

    public void release() throws IOException {
        assert refCount.get() > 0;
        final int refs = refCount.decrementAndGet();
        if (refs == 0) {
            source.close();
        }
    }

    @Override
    public String toString() {
        return "Table" +
                "{source='" + source + '\'' +
                ", comparator=" + comparator +
                '}';
    }

    @Override
    public void close() throws IOException {
        release();
    }
}
