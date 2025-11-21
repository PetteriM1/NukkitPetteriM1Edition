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
import org.iq80.leveldb.util.Closeables;
import org.iq80.leveldb.util.Slice;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

public final class MergingIterator extends ASeekingIterator<InternalKey, Slice>
        implements InternalIterator {
    private final List<InternalIterator> iterators;
    private final Comparator<InternalKey> keyComparator;
    private final Comparator<InternalIterator> iteratorComparator;
    private PriorityQueue<InternalIterator> queue;
    private InternalIterator current;

    public MergingIterator(List<InternalIterator> iterators, Comparator<InternalKey> comparator) {
        this.keyComparator = comparator;
        this.iteratorComparator = (o1, o2) -> keyComparator.compare(o1.key(), o2.key());
        this.iterators = iterators;
    }

    @Override
    public void internalClose() throws IOException {
        Closeables.closeAll(iterators);
    }

    @Override
    protected InternalKey internalKey() {
        return current.key();
    }

    @Override
    protected boolean internalNext(boolean switchDirection) {
        if (switchDirection) {
            InternalKey key = key();
            rebuildQueue(false, iter -> iter != current && iter.seek(key) && (keyComparator.compare(key, iter.key()) != 0 || iter.next()));
        }
        if (current.next()) {
            queue.add(current);
        }
        current = queue.poll();
        return current != null;
    }

    @Override
    protected boolean internalPrev(boolean switchDirection) {
        if (switchDirection) {
            InternalKey key = key();
            rebuildQueue(true, iter -> {
                if (iter.seek(key)) {
                    // Child is at first entry >= key().  Step back one to be < key()
                    return iter.prev();
                } else {
                    // Child has no entries >= key().  Position at last entry.
                    return iter.seekToLast();
                }
            });
        } else {
            if (current.prev()) {
                queue.add(current);
            }
        }
        current = queue.poll();
        return current != null;
    }

    @Override
    protected boolean internalSeek(InternalKey targetKey) {
        rebuildQueue(false, itr -> itr.seek(targetKey));
        current = queue.poll();
        return current != null;
    }

    @Override
    protected boolean internalSeekToFirst() {
        rebuildQueue(false, SeekingIterator::seekToFirst);
        current = queue.poll();
        return current != null;
    }

    @Override
    protected boolean internalSeekToLast() {
        rebuildQueue(true, SeekingIterator::seekToLast);
        current = queue.poll();
        return current != null;
    }

    @Override
    protected Slice internalValue() {
        return current.value();
    }

    private void rebuildQueue(boolean reverse, Function<InternalIterator, Boolean> func) {
        this.queue = new PriorityQueue<>(iterators.size(), reverse ? iteratorComparator.reversed() : iteratorComparator);
        for (InternalIterator iterator : iterators) {
            if (func.apply(iterator)) {
                queue.add(iterator);
            }
        }
    }
}
