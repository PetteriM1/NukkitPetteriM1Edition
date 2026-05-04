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

import org.iq80.leveldb.DBException;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.util.Slice;
import org.iq80.leveldb.util.Slices;

import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class DBIteratorAdapter
        implements DBIterator {
    private static final String ILLEGAL_STATE = "Illegal use of iterator after release";
    private final SnapshotSeekingIterator seekingIterator;
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private Direction direction = Direction.FORWARD;
    private DbEntry elem;

    public DBIteratorAdapter(SnapshotSeekingIterator seekingIterator) {
        this.seekingIterator = seekingIterator;
    }

    public static class DbEntry
            implements Entry<byte[], byte[]> {
        private final Slice key;
        private final Slice value;

        public DbEntry(Slice key, Slice value) {
            Objects.requireNonNull(key);
            Objects.requireNonNull(value);
            this.key = key;
            this.value = value;
        }

        @Override
        public byte[] getKey() {
            return key.getBytes();
        }

        public Slice getKeySlice() {
            return key;
        }

        @Override
        public byte[] getValue() {
            return value.getBytes();
        }

        public Slice getValueSlice() {
            return value;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Entry) {
                Entry<?, ?> that = (Entry<?, ?>) object;
                return key.equals(that.getKey()) &&
                        value.equals(that.getValue());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return key.hashCode() ^ value.hashCode();
        }

        @Override
        public byte[] setValue(byte[] value) {
            throw new UnsupportedOperationException();
        }

        /**
         * Returns a string representation of the form <code>{key}={value}</code>.
         */
        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    @Override
    public void close() {
        // This is an end user API.. he might screw up and close multiple times.
        // but we don't want the close multiple times as reference counts go bad.
        if (closed.compareAndSet(false, true)) {
            direction = Direction.RELEASED;
            seekingIterator.close();
        }
    }

    @Override
    public boolean hasNext() {
        if (direction == Direction.RELEASED) {
            throw new DBException(ILLEGAL_STATE);
        }
        if (direction != Direction.FORWARD) {
            elem = null;
            direction = Direction.FORWARD;
        }
        if (elem == null) {
            elem = seekingIterator.next() ? new DbEntry(seekingIterator.key(), seekingIterator.value()) : null;
        }
        return elem != null;
    }

    @Override
    public boolean hasPrev() {
        if (direction == Direction.RELEASED) {
            throw new DBException(ILLEGAL_STATE);
        }
        if (direction != Direction.REVERSE) {
            elem = null;
            direction = Direction.REVERSE;
        }
        if (elem == null) {
            elem = seekingIterator.prev() ? new DbEntry(seekingIterator.key(), seekingIterator.value()) : null;
        }
        return elem != null;
    }

    @Override
    public DbEntry next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        DbEntry elem = this.elem;
        this.elem = null;
        return elem;
    }

    @Override
    public DbEntry peekNext() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return elem;
    }

    @Override
    public DbEntry peekPrev() {
        if (!hasPrev()) {
            throw new NoSuchElementException();
        }
        return this.elem;
    }

    @Override
    public DbEntry prev() {
        if (!hasPrev()) {
            throw new NoSuchElementException();
        }
        DbEntry elem = this.elem;
        this.elem = null;
        return elem;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void seek(byte[] targetKey) {
        if (direction == Direction.RELEASED) {
            throw new DBException(ILLEGAL_STATE);
        }
        direction = Direction.FORWARD;
        elem = seekingIterator.seek(Slices.wrappedBuffer(targetKey)) ? new DbEntry(seekingIterator.key(), seekingIterator.value()) : null;
    }

    @Override
    public void seekToFirst() {
        if (direction == Direction.RELEASED) {
            throw new DBException(ILLEGAL_STATE);
        }
        direction = Direction.FORWARD;
        elem = seekingIterator.seekToFirst() ? new DbEntry(seekingIterator.key(), seekingIterator.value()) : null;
    }

    @Override
    public void seekToLast() {
        if (direction == Direction.RELEASED) {
            throw new DBException(ILLEGAL_STATE);
        }
        direction = Direction.REVERSE;
        elem = seekingIterator.seekToLast() ? new DbEntry(seekingIterator.key(), seekingIterator.value()) : null;
    }
}
