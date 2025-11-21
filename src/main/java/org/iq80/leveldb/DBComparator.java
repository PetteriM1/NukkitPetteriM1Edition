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
package org.iq80.leveldb;

import java.util.Comparator;

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public interface DBComparator
        extends Comparator<byte[]> {
    /**
     * returns a 'short key' where the 'short key' is greater than or equal to key.
     * Simple comparator implementations should return key unchanged,
     * i.e., an implementation of this method that does nothing is correct.
     */
    byte[] findShortSuccessor(byte[] key);

    /**
     * If {@code start < limit}, returns a short key in {@code [start,limit)}.
     * Simple comparator implementations should return start unchanged,
     * i.e., an implementation of this method that does nothing is correct.
     */
    byte[] findShortestSeparator(byte[] start, byte[] limit);

    /**
     * The name of the comparator.  Used to check for comparator
     * mismatches (i.e., a DB created with one comparator is
     * accessed using a different comparator.
     * <p>
     * The client of this package should switch to a new name whenever
     * the comparator implementation changes in a way that will cause
     * the relative ordering of any two keys to change.
     * <p
     * Names starting with "leveldb." are reserved and should not be used
     * by any clients of this package.
     *
     * @return comparator name
     */
    String name();
}
