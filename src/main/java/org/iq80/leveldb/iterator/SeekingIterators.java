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

import org.iq80.leveldb.impl.InternalKey;
import org.iq80.leveldb.util.Slice;

import java.io.Closeable;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * When ever possible a specific interface implementation is created for speed purpose.
 * see {@link DbIterator} where the same approach is used.
 */
public final class SeekingIterators {
    private SeekingIterators() {
        //utility
    }

    private static class InternalTwoLevelIterator<T> extends TwoLevelIterator<T, InternalKey, Slice> implements InternalIterator {
        InternalTwoLevelIterator(SeekingIterator<InternalKey, T> indexIterator, Function<T, SeekingIterator<InternalKey, Slice>> blockFunction, Closeable closeableResources) {
            super(indexIterator, blockFunction, closeableResources);
        }
    }

    private static class SliceTwoLevelIterator extends TwoLevelIterator<Slice, Slice, Slice> implements SliceIterator {
        SliceTwoLevelIterator(SliceIterator indexIterator, Function<Slice, SeekingIterator<Slice, Slice>> blockFunction, Closeable closeableResources) {
            super(indexIterator, blockFunction, closeableResources);
        }
    }

    /**
     * Seeking iterator based on provided sorted list. Unpredictable behavior
     * will happen if {@code list} is not sorted according to {@code comparator}
     */
    public static <T, K, V> SeekingIterator<K, V> fromSortedList(List<T> list, Function<T, K> keyExtractor, Function<T, V> valueExtractor, Comparator<K> comparator) {
        return new SortedCollectionIterator<>(list, keyExtractor, valueExtractor, comparator);
    }

    public static <T> InternalIterator twoLevelInternalIterator(SeekingIterator<InternalKey, T> indexIterator, Function<T, SeekingIterator<InternalKey, Slice>> blockFunction, Closeable closeableResources) {
        return new InternalTwoLevelIterator<>(indexIterator, blockFunction, closeableResources);
    }

    public static SliceIterator twoLevelSliceIterator(SliceIterator indexIterator, Function<Slice, SeekingIterator<Slice, Slice>> blockFunction, Closeable closeableResources) {
        return new SliceTwoLevelIterator(indexIterator, blockFunction, closeableResources);
    }
}
