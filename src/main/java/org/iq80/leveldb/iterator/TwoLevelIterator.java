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
package org.iq80.leveldb.iterator;

import org.iq80.leveldb.DBException;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Function;

/**
 * Equivalent to TwoLevelIterator int google leveldb
 */
class TwoLevelIterator<T, K, V>
        extends ASeekingIterator<K, V> {
    private final Function<T, SeekingIterator<K, V>> blockFunction;
    private Closeable closeableResources;
    private SeekingIterator<K, T> indexIterator;
    private SeekingIterator<K, V> current;

    TwoLevelIterator(SeekingIterator<K, T> indexIterator, Function<T, SeekingIterator<K, V>> blockFunction, Closeable closeableResources) {
        this.indexIterator = indexIterator;
        this.blockFunction = blockFunction;
        this.closeableResources = closeableResources;
    }

    @Override
    protected boolean internalSeekToFirst() {
        if (initDataBlock(indexIterator.seekToFirst()) && current.seekToFirst()) {
            return true;
        }
        return skipEmptyDataBlocksForward();
    }

    @Override
    protected boolean internalSeek(K targetKey) {
        // seek the index to the block containing the key
        // if indexIterator does not have a next, it mean the key does not exist in this iterator
        if (initDataBlock(indexIterator.seek(targetKey)) && current.seek(targetKey)) {
            return true;
        }
        return skipEmptyDataBlocksForward();
    }

    @Override
    protected boolean internalSeekToLast() {
        if (!indexIterator.seekToLast()) {
            closeAndResetCurrent();
            return false;
        }
        if (initDataBlock(true) && current.seekToLast()) {
            return true;
        }
        return skipEmptyDataBlocksBackward();
    }

    @Override
    protected boolean internalNext(boolean switchDirection) {
        return current.next() || skipEmptyDataBlocksForward();
    }

    @Override
    protected boolean internalPrev(boolean switchDirection) {
        return current.prev() || skipEmptyDataBlocksBackward();
    }

    @Override
    protected K internalKey() {
        return current.key();
    }

    @Override
    protected V internalValue() {
        return current.value();
    }

    private boolean skipEmptyDataBlocksForward() {
        while (current == null || !current.valid()) {
            if (!indexIterator.valid()) {
                closeAndResetCurrent();
                return false;
            }
            if (initDataBlock(indexIterator.next()) && current.seekToFirst()) {
                return true;
            }
        }
        return true;
    }

    private boolean skipEmptyDataBlocksBackward() {
        while (current == null || !current.valid()) {
            if (!indexIterator.valid()) {
                closeAndResetCurrent();
                return false;
            }
            if (initDataBlock(indexIterator.prev()) && current.seekToLast()) {
                return true;
            }
        }
        return true;
    }

    private boolean initDataBlock(boolean valid) {
        closeAndResetCurrent();
        if (valid) {
            // seek the current iterator to the key
            T blockHandle = indexIterator.value();
            current = blockFunction.apply(blockHandle);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "TwoLevelIterator{" +
                "blockFunction=" + blockFunction +
                ", indexIterator=" + indexIterator +
                ", current=" + current +
                '}';
    }

    private void closeAndResetCurrent() {
        if (current != null) {
            try {
                current.close();
            } catch (IOException e) {
                throw new DBException(e);
            }
        }
        current = null;
    }

    @Override
    public void internalClose() throws IOException {
        assert closeableResources != null : "Unexpected multiple calls to close() method";
        try {
            closeAndResetCurrent();
            this.indexIterator.close();
            this.indexIterator = null;
        } finally {
            closeableResources.close();
            closeableResources = null;
        }
    }
}
