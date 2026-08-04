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

import com.google.common.base.Throwables;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Callable;

public final class Closeables {
    private Closeables() {
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }

    public static void closeAll(Iterable<? extends Closeable> closeables) throws IOException {
        Throwable throwable = null;
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (Throwable e) {
                if (throwable == null) {
                    throwable = e;
                } else {
                    throwable.addSuppressed(e);
                }
            }
        }

        if (throwable != null) {
            Throwables.propagateIfPossible(throwable, IOException.class);
            throw new AssertionError(throwable); // not possible
        }
    }

    /**
     * Create a wrapper for {@code resource}. If wrapper fail to be created, resource is properly closed.
     * In the case if {@code wrapperFactory.call()} succeed, returned object is responsible to close {@code resource}.
     *
     * @param wrapperFactory wrapper factory
     * @param resource       resource used by wrapper
     * @param <T>            wrapper object type
     * @return resource wrapper instance
     * @throws IOException in the case of any exception.
     */
    public static <T> T wrapResource(Callable<T> wrapperFactory, Closeable resource) throws IOException {
        try {
            return wrapperFactory.call();
        } catch (Throwable throwable) {
            try {
                resource.close();
            } catch (Throwable e1) {
                throwable.addSuppressed(e1);
            }
            Throwables.propagateIfPossible(throwable, IOException.class);
            throw new AssertionError(throwable); // not possible
        }
    }
}
