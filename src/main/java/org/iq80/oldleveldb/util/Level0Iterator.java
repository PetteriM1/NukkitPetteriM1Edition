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
package org.iq80.oldleveldb.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Iterables;
import org.iq80.oldleveldb.impl.FileMetaData;
import org.iq80.oldleveldb.impl.InternalKey;
import org.iq80.oldleveldb.impl.SeekingIterator;
import org.iq80.oldleveldb.impl.TableCache;

import java.util.*;
import java.util.Map.Entry;

public final class Level0Iterator
        extends AbstractSeekingIterator<InternalKey, Slice>
        implements InternalIterator {
    private final List<InternalTableIterator> inputs;
    private final PriorityQueue<ComparableIterator> priorityQueue;
    private final Comparator<InternalKey> comparator;

    public Level0Iterator(TableCache tableCache, List<FileMetaData> files, Comparator<InternalKey> comparator) {
        Builder<InternalTableIterator> builder = ImmutableList.builder();
        for (FileMetaData file : files) {
            builder.add(tableCache.newIterator(file));
        }
        this.inputs = builder.build();
        this.comparator = comparator;

        this.priorityQueue = new PriorityQueue<>(Iterables.size(inputs) + 1);
        resetPriorityQueue(comparator);
    }

    private static class ComparableIterator
            implements Iterator<Entry<InternalKey, Slice>>, Comparable<ComparableIterator> {
        private final SeekingIterator<InternalKey, Slice> iterator;
        private final Comparator<InternalKey> comparator;
        private final int ordinal;
        private Entry<InternalKey, Slice> nextElement;

        private ComparableIterator(SeekingIterator<InternalKey, Slice> iterator, Comparator<InternalKey> comparator, int ordinal, Entry<InternalKey, Slice> nextElement) {
            this.iterator = iterator;
            this.comparator = comparator;
            this.ordinal = ordinal;
            this.nextElement = nextElement;
        }

        @Override
        public int compareTo(ComparableIterator that) {
            int result = comparator.compare(this.nextElement.getKey(), that.nextElement.getKey());
            if (result == 0) {
                result = Integer.compare(this.ordinal, that.ordinal);
            }
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ComparableIterator comparableIterator = (ComparableIterator) o;

            if (ordinal != comparableIterator.ordinal) {
                return false;
            }
            if (nextElement != null ? !nextElement.equals(comparableIterator.nextElement) : comparableIterator.nextElement != null) {
                return false;
            }

            return true;
        }

        @Override
        public boolean hasNext() {
            return nextElement != null;
        }

        @Override
        public int hashCode() {
            int result = ordinal;
            result = 31 * result + (nextElement != null ? nextElement.hashCode() : 0);
            return result;
        }

        @Override
        public Entry<InternalKey, Slice> next() {
            if (nextElement == null) {
                throw new NoSuchElementException();
            }

            Entry<InternalKey, Slice> result = nextElement;
            if (iterator.hasNext()) {
                nextElement = iterator.next();
            } else {
                nextElement = null;
            }
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    protected Entry<InternalKey, Slice> getNextElement() {
        Entry<InternalKey, Slice> result = null;
        ComparableIterator nextIterator = priorityQueue.poll();
        if (nextIterator != null) {
            result = nextIterator.next();
            if (nextIterator.hasNext()) {
                priorityQueue.add(nextIterator);
            }
        }
        return result;
    }

    private void resetPriorityQueue(Comparator<InternalKey> comparator) {
        int i = 0;
        for (InternalTableIterator input : inputs) {
            if (input.hasNext()) {
                priorityQueue.add(new ComparableIterator(input, comparator, i++, input.next()));
            }
        }
    }

    @Override
    protected void seekInternal(InternalKey targetKey) {
        for (InternalTableIterator input : inputs) {
            input.seek(targetKey);
        }
        resetPriorityQueue(comparator);
    }

    @Override
    protected void seekToFirstInternal() {
        for (InternalTableIterator input : inputs) {
            input.seekToFirst();
        }
        resetPriorityQueue(comparator);
    }

    @Override
    public String toString() {
        return "MergingIterator" +
                "{inputs=" + Iterables.toString(inputs) +
                ", comparator=" + comparator +
                '}';
    }
}
