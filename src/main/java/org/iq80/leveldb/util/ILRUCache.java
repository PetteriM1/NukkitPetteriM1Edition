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
package org.iq80.leveldb.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public interface ILRUCache<K, V> {
    long getApproximateMemoryUsage();

    /**
     * Get a value from cache if present (already loaded)
     *
     * @param key cache key
     * @return value if present, {@ode null} otherwise
     */
    V getIfPresent(K key);

    /**
     * Discards all entries in the cache.
     */
    void invalidateAll();

    /**
     * Get cached valued by key or load and cache loaded value.
     *
     * @param key    cache key
     * @param loader key value loader
     * @return loaded/saved value
     * @throws ExecutionException if load has any exception.
     */
    V load(final K key, Callable<V> loader) throws ExecutionException;
}
