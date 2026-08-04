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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Weigher;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * LRU cache with special weigher to count correctly Slice weight.
 *
 * @author Honore Vasconcelos
 */
public final class LRUCache<K, V>
        implements ILRUCache<K, V> {
    private final Cache<K, V> cache;
    private final Weigher<K, V> weigher;

    private LRUCache(int capacity, final Weigher<K, V> weigher) {
        this.cache = CacheBuilder.newBuilder()
                .maximumWeight(capacity)
                .weigher(weigher)
                .concurrencyLevel(1 << 4)
                .build();
        this.weigher = weigher;
    }

    public static <K, V> ILRUCache<K, V> createCache(int capacity, final Weigher<K, V> weigher) {
        return new LRUCache<>(capacity, weigher);
    }

    public V load(final K key, Callable<V> loader) throws ExecutionException {
        return cache.get(key, loader);
    }

    @Override
    public long getApproximateMemoryUsage() {
        return cache.asMap().entrySet().stream()
                .mapToLong(e -> weigher.weigh(e.getKey(), e.getValue()))
                .sum();
    }

    @Override
    public V getIfPresent(K key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void invalidateAll() {
        cache.invalidateAll();
    }
}
