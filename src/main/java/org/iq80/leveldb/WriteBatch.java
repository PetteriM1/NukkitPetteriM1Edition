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

import java.io.Closeable;

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public interface WriteBatch
        extends Closeable {
    /**
     * The size of the database changes caused by this batch.
     * <p>
     * This number is tied to implementation details, and may change across
     * releases. It is intended for LevelDB usage metrics.
     */
    int getApproximateSize();

    /**
     * Number of entries in the batch
     */
    int size();

    /**
     * Store the mapping key and value in the database.
     */
    WriteBatch put(byte[] key, byte[] value);

    /**
     * If the database contains a mapping for "key", erase it.  Else do nothing.
     */
    WriteBatch delete(byte[] key);
}
