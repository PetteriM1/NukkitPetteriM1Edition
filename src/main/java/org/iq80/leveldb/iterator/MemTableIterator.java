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

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import org.iq80.leveldb.impl.InternalKey;
import org.iq80.leveldb.util.Slice;

import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;

public final class MemTableIterator
        extends ASeekingIterator<InternalKey, Slice> implements InternalIterator {
    private PeekingIterator<Map.Entry<InternalKey, Slice>> iterator;
    private Map.Entry<InternalKey, Slice> entry;
    private final ConcurrentNavigableMap<InternalKey, Slice> table;

    public MemTableIterator(ConcurrentNavigableMap<InternalKey, Slice> table) {
        this.table = table;
    }

    @Override
    public void internalClose() {
        iterator = null;
    }

    @Override
    protected InternalKey internalKey() {
        return entry.getKey();
    }

    @Override
    protected boolean internalNext(boolean switchDirection) {
        if (switchDirection) {
            iterator = Iterators.peekingIterator(table.tailMap(entry.getKey()).entrySet().iterator());
            if (iterator.hasNext()) {
                iterator.next(); //skip "entry"
            }
        }
        entry = iterator.hasNext() ? iterator.next() : null;
        return entry != null;
    }

    @Override
    protected boolean internalPrev(boolean switchDirection) {
        if (switchDirection) {
            iterator = Iterators.peekingIterator(table.descendingMap().tailMap(entry.getKey()).entrySet().iterator());
            if (iterator.hasNext()) {
                iterator.next(); //skip "entry"
            }
        }
        entry = iterator.hasNext() ? iterator.next() : null;
        return entry != null;
    }

    @Override
    protected boolean internalSeek(InternalKey targetKey) {
        iterator = Iterators.peekingIterator(table.tailMap(targetKey).entrySet().iterator());
        entry = iterator.hasNext() ? iterator.next() : null;
        return entry != null;
    }

    @Override
    protected boolean internalSeekToFirst() {
        iterator = Iterators.peekingIterator(table.entrySet().iterator());
        entry = iterator.hasNext() ? iterator.next() : null;
        return entry != null;
    }

    @Override
    protected boolean internalSeekToLast() {
        iterator = Iterators.peekingIterator(table.descendingMap().entrySet().iterator());
        entry = iterator.hasNext() ? iterator.next() : null;
        return entry != null;
    }

    @Override
    protected Slice internalValue() {
        return entry.getValue();
    }
}
