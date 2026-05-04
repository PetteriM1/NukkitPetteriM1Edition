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

import com.google.common.collect.ImmutableList;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * If for some reason {@link #build()} is not called (eg: due to some exception) when {@link SafeListBuilder#close()}
 * is called, then {@link Closeable#close()} method will be called on each inserted items.
 *
 * @param <T> entry type that should be closed if nop properly read.
 */
public final class SafeListBuilder<T extends Closeable> implements Closeable {
    private ImmutableList.Builder<T> builder;

    private SafeListBuilder(ImmutableList.Builder<T> builder) {
        this.builder = builder;
    }

    /**
     * Adds {@code element} to the {@code ImmutableList}.
     *
     * @param element the element to add
     * @return this {@code Builder} object
     * @throws NullPointerException if {@code element} is null
     */
    public ImmutableList.Builder<T> add(T element) {
        return builder.add(element);
    }

    /**
     * Adds each element of {@code elements} to the {@code ImmutableList}.
     *
     * @param elements the {@code Iterable} to add to the {@code ImmutableList}
     * @return this {@code Builder} object
     * @throws NullPointerException if {@code elements} is null or contains a
     *                              null element
     */
    public ImmutableList.Builder<T> addAll(Iterable<? extends T> elements) {
        return builder.addAll(elements);
    }

    /**
     * Returns a newly-created {@code ImmutableList} based on the contents of
     * the {@code SafeListBuilder}.
     * <p>
     * After this call, {@link #close()} wont have any effect on item of the list.
     */
    public List<T> build() {
        final ImmutableList.Builder<T> b1 = this.builder;
        this.builder = null;
        return b1.build();
    }

    /**
     * Returns a new builder.
     */
    public static <T extends Closeable> SafeListBuilder<T> builder() {
        return new SafeListBuilder<>(ImmutableList.builder());
    }

    /**
     * If {@link #build()} was not called yet, {@link Closeable#close()} method will be called on all items added
     * trough {@link #add(Closeable)} and {@link #addAll(Iterable)}.
     * If {@link #build()} was already used, nothing will happen on the data of this builder.
     */
    @Override
    public void close() throws IOException {
        final ImmutableList.Builder<T> b = this.builder;
        if (b != null) {
            Closeables.closeAll(b.build());
        }
    }
}
