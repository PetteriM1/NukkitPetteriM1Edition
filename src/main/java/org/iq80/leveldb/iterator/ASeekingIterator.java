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

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Seeking Iterator base implementation that ensure proper state validation before
 * each call and implement shared direction management between iterator implementations.
 *
 * @param <K> type of the key
 * @param <V> type of the value
 */
public abstract class ASeekingIterator<K, V> implements SeekingIterator<K, V> {
    private static final String RELEASED_EXCEPTION = "Illegal use of iterator after release";
    private Direction direction = Direction.START_OF_ITERATOR;

    @Override
    public final void close() {
        if (direction != Direction.RELEASED) {
            direction = Direction.RELEASED;
            try {
                internalClose();
            } catch (IOException e) {
                throw new DBException(e);
            }
        } else {
            throw new DBException("Releasing iterator more than once");
        }
    }

    protected abstract void internalClose() throws IOException;

    protected abstract K internalKey();

    protected abstract boolean internalNext(boolean switchDirection);

    protected abstract boolean internalPrev(boolean switchDirection);

    protected abstract boolean internalSeek(K key);

    protected abstract boolean internalSeekToFirst();

    protected abstract boolean internalSeekToLast();

    protected abstract V internalValue();

    @Override
    public final K key() {
        if (!direction.isValid()) {
            throw new NoSuchElementException();
        }
        return internalKey();
    }

    @Override
    public final boolean next() {
        switch (direction) {
            case START_OF_ITERATOR:
                return seekToFirst();
            case RELEASED:
                throw new DBException(RELEASED_EXCEPTION);
            case END_OF_ITERATOR:
                return false;
        }
        boolean switchDirection = direction == Direction.REVERSE;
        if (internalNext(switchDirection)) {
            if (switchDirection) {
                direction = Direction.FORWARD;
            }
            return true;
        }
        direction = Direction.END_OF_ITERATOR;
        return false;
    }

    @Override
    public final boolean prev() {
        switch (direction) {
            case RELEASED:
                throw new DBException(RELEASED_EXCEPTION);
            case START_OF_ITERATOR:
                return false;
            case END_OF_ITERATOR:
                return seekToLast();
        }
        boolean switchDirection = direction == Direction.FORWARD;
        if (internalPrev(switchDirection)) {
            if (switchDirection) {
                direction = Direction.REVERSE;
            }
            return true;
        }
        direction = Direction.START_OF_ITERATOR;
        return false;
    }

    @Override
    public final boolean seek(K key) {
        if (direction == Direction.RELEASED) {
            throw new DBException(RELEASED_EXCEPTION);
        }
        if (internalSeek(key)) {
            direction = Direction.FORWARD;
            return true;
        }
        direction = Direction.END_OF_ITERATOR;
        return false;
    }

    @Override
    public final boolean seekToFirst() {
        if (direction == Direction.RELEASED) {
            throw new DBException(RELEASED_EXCEPTION);
        }
        if (internalSeekToFirst()) {
            direction = Direction.FORWARD;
            return true;
        }
        this.direction = Direction.END_OF_ITERATOR;
        return false;
    }

    @Override
    public final boolean seekToLast() {
        if (direction == Direction.RELEASED) {
            throw new DBException(RELEASED_EXCEPTION);
        }
        if (internalSeekToLast()) {
            direction = Direction.REVERSE;
            return true;
        }
        this.direction = Direction.START_OF_ITERATOR;
        return false;
    }

    @Override
    public final boolean valid() {
        return direction.isValid();
    }

    @Override
    public final V value() {
        if (!direction.isValid()) {
            throw new NoSuchElementException();
        }
        return internalValue();
    }
}
