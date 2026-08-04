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

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

class SortedCollectionIterator<T, K, V> extends ASeekingIterator<K, V> {
    private final List<T> entries;
    private final Function<T, K> keyExtractor;
    private final Function<T, V> valueExtractor;
    private final Comparator<K> comparator;
    private int index;

    SortedCollectionIterator(List<T> entries, Function<T, K> keyExtractor, Function<T, V> valueExtractor, Comparator<K> comparator) {
        this.entries = entries;
        this.keyExtractor = keyExtractor;
        this.valueExtractor = valueExtractor;
        this.comparator = comparator;
        this.index = entries.size();
    }

    @Override
    public void internalClose() {
        //na
    }

    @Override
    protected boolean internalSeekToFirst() {
        if (entries.isEmpty()) {
            return false;
        }
        index = 0;
        return true;
    }

    @Override
    protected boolean internalSeekToLast() {
        if (entries.isEmpty()) {
            return false;
        } else {
            index = entries.size() - 1;
            return true;
        }
    }

    @Override
    protected boolean internalSeek(K targetKey) {
        // seek the index to the block containing the
        if (entries.isEmpty()) {
            return false;
        }

        // todo replace with Collections.binarySearch
        //Collections.binarySearch(entries, comparator)
        int left = 0;
        int right = entries.size() - 1;

        // binary search restart positions to find the restart position immediately before the targetKey
        while (left < right) {
            int mid = (left + right) >> 1;

            if (comparator.compare(keyExtractor.apply(entries.get(mid)), targetKey) < 0) {
                // Key at "mid.largest" is < "target".  Therefore all
                // files at or before "mid" are uninteresting.
                left = mid + 1;
            } else {
                // Key at "mid.largest" is >= "target".  Therefore all files
                // after "mid" are uninteresting.
                right = mid;
            }
        }
        index = right;

        // if the index is now pointing to the last block in the file, check if the largest key
        // in the block is than the the target key.  If so, we need to seek beyond the end of this file
        if (index == entries.size() - 1 && comparator.compare(keyExtractor.apply(entries.get(index)), targetKey) < 0) {
            index++;
        }
        return index < entries.size();
    }

    @Override
    protected boolean internalNext(boolean switchDirection) {
        index++;
        return index < entries.size();
    }

    @Override
    protected boolean internalPrev(boolean switchDirection) {
        if (index == 0) {
            return false;
        } else {
            index--;
            return true;
        }
    }

    @Override
    protected K internalKey() {
        return keyExtractor.apply(entries.get(index));
    }

    @Override
    protected V internalValue() {
        return valueExtractor.apply(entries.get(index));
    }
}
