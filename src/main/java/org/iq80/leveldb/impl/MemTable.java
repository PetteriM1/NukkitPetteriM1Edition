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

import org.iq80.leveldb.iterator.MemTableIterator;
import org.iq80.leveldb.util.Slice;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.iq80.leveldb.util.SizeOf.SIZE_OF_LONG;

public class MemTable {
    private final ConcurrentSkipListMap<InternalKey, Slice> table;
    private final AtomicLong approximateMemoryUsage = new AtomicLong();

    public MemTable(InternalKeyComparator internalKeyComparator) {
        table = new ConcurrentSkipListMap<>(internalKeyComparator);
    }

    public void add(long sequenceNumber, ValueType valueType, Slice key, Slice value) {
        if (valueType == null) throw new NullPointerException("valueType is null");
        if (key == null) throw new NullPointerException("key is null");
        if (value == null) throw new NullPointerException("value is null");

        InternalKey internalKey = new InternalKey(key, sequenceNumber, valueType);
        table.put(internalKey, value);

        approximateMemoryUsage.addAndGet(key.length() + SIZE_OF_LONG + value.length());
    }

    public long approximateMemoryUsage() {
        return approximateMemoryUsage.get();
    }

    public LookupResult get(LookupKey key) {
        if (key == null) throw new NullPointerException("key is null");

        InternalKey internalKey = key.getInternalKey();
        Entry<InternalKey, Slice> entry = table.ceilingEntry(internalKey);
        if (entry == null) {
            return null;
        }

        InternalKey entryKey = entry.getKey();
        if (entryKey.getUserKey().equals(key.getUserKey())) {
            if (entryKey.getValueType() == ValueType.DELETION) {
                return LookupResult.deleted(key);
            } else {
                return LookupResult.ok(key, entry.getValue());
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return table.isEmpty();
    }

    public MemTableIterator iterator() {
        return new MemTableIterator(table);
    }
}
