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

import java.io.Closeable;

/**
 * Seeking iterator that is very similar to original implementation,
 * with the distinction that all methods return the state (valid/invalid)
 * of the iterator.
 *
 * @param <K> type of the key
 * @param <V> type of the value
 */
public interface SeekingIterator<K, V> extends Closeable {
    /**
     * An iterator is either positioned at a key/value pair, or
     * not valid.
     *
     * @return true if the iterator is valid.
     */
    boolean valid();

    /**
     * Position at the first key in the source.  The iterator is {@link #valid()}
     * after this call if the source is not empty.
     *
     * @return {@code true} if iterator is valid, same value will be return by {@link #valid()} after this call
     **/
    boolean seekToFirst();

    /**
     * Position at the last key in the source.  The iterator is
     * {@link #valid()} after this call if the source is not empty.
     *
     * @return {@code true} if iterator is valid, same value will be return by {@link #valid()} after this call
     **/
    boolean seekToLast();

    /**
     * Position at the first key in the source that is at or past target.
     * The iterator is {@link #valid()} after this call if the source contains
     * an entry that comes at or past target.
     *
     * @return {@code true} if iterator is valid, same value will be return by {@link #valid()} after this call
     **/
    boolean seek(K key);

    /**
     * Moves to the next entry in the source.  After this call, {@link #valid()} is
     * true if the iterator was not positioned at the last entry in the source.
     * In the case {@link #seek(Object)}, {@link #seekToLast()} or {@link #seekToLast()} where not called
     * first call to this method should position iterator on the first entry.
     *
     * @return {@code true} if iterator is valid, same value will be return by {@link #valid()} after this call
     **/
    boolean next();

    /**
     * Moves to the previous entry in the source. Return true if the iterator was
     * not positioned at the first entry in source.
     *
     * @return {@code true} if iterator is valid, same value will be return by {@link #valid()} after this call
     **/
    boolean prev();

    /**
     * Return the key for the current entry.  The underlying storage for
     * the returned slice is valid only until the next modification of
     * the iterator.
     *
     * @return current position key
     * @throws java.util.NoSuchElementException if iterator is not in valid state
     **/
    K key();

    /**
     * Return the value for the current entry.  The underlying storage for
     * the returned slice is valid only until the next modification of
     * the iterator.
     *
     * @return current position value
     * @throws java.util.NoSuchElementException if iterator is not in valid state
     **/
    V value();
}
