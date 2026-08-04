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

import java.io.IOException;

public class InternalTableIterator
        implements InternalIterator {
    private final SliceIterator tableIterator;
    private InternalKey key;

    public InternalTableIterator(SliceIterator tableIterator) {
        this.tableIterator = tableIterator;
    }

    @Override
    public boolean valid() {
        return tableIterator.valid();
    }

    @Override
    public boolean seekToFirst() {
        this.key = null;
        return tableIterator.seekToFirst();
    }

    public boolean seek(InternalKey targetKey) {
        this.key = null;
        return tableIterator.seek(targetKey.encode());
    }

    @Override
    public boolean seekToLast() {
        this.key = null;
        return tableIterator.seekToLast();
    }

    @Override
    public boolean next() {
        this.key = null;
        return tableIterator.next();
    }

    @Override
    public boolean prev() {
        this.key = null;
        return tableIterator.prev();
    }

    @Override
    public InternalKey key() {
        if (key == null) {
            //cache key decomposition
            this.key = new InternalKey(tableIterator.key());
        }
        return this.key;
    }

    @Override
    public Slice value() {
        return tableIterator.value();
    }

    @Override
    public String toString() {
        return "InternalTableIterator" +
                "{fromIterator=" + tableIterator +
                '}';
    }

    @Override
    public void close() throws IOException {
        this.key = null;
        tableIterator.close();
    }
}
