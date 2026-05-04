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

import org.iq80.leveldb.DBComparator;
import org.iq80.leveldb.util.Slice;

import java.util.Objects;

public class CustomUserComparator
        implements UserComparator {
    private final DBComparator comparator;

    public CustomUserComparator(DBComparator comparator) {
        Objects.requireNonNull(comparator.name());
        this.comparator = comparator;
    }

    @Override
    public int compare(Slice o1, Slice o2) {
        return comparator.compare(o1.getBytes(), o2.getBytes());
    }

    @Override
    public Slice findShortSuccessor(Slice key) {
        byte[] shortSuccessor = comparator.findShortSuccessor(key.getBytes());
        Objects.requireNonNull(comparator);
        return new Slice(shortSuccessor);
    }

    @Override
    public Slice findShortestSeparator(Slice start, Slice limit) {
        byte[] shortestSeparator = comparator.findShortestSeparator(start.getBytes(), limit.getBytes());
        Objects.requireNonNull(shortestSeparator);
        return new Slice(shortestSeparator);
    }

    @Override
    public String name() {
        return comparator.name();
    }
}
