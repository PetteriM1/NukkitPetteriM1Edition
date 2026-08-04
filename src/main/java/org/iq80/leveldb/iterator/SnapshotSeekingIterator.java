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

import com.google.common.base.Preconditions;
import org.iq80.leveldb.impl.InternalKey;
import org.iq80.leveldb.impl.ValueType;
import org.iq80.leveldb.util.Slice;

import java.io.IOException;
import java.util.Comparator;

//DbIter
public final class SnapshotSeekingIterator
        extends ASeekingIterator<Slice, Slice> {
    private final InternalIterator iterator;
    private final long sequence;
    private final Comparator<Slice> userComparator;
    private final IRecordBytesListener listener;
    private Slice key;
    private Slice value;

    public SnapshotSeekingIterator(InternalIterator iterator, long sequence, Comparator<Slice> userComparator, IRecordBytesListener listener) {
        this.iterator = iterator;
        this.sequence = sequence;
        this.userComparator = userComparator;
        this.listener = listener;
    }

    @Override
    protected void internalClose() throws IOException {
        iterator.close();
    }

    @Override
    protected boolean internalSeekToFirst() {
        return iterator.seekToFirst() && findNextUserEntry(false, null);
    }

    @Override
    protected boolean internalSeekToLast() {
        return iterator.seekToLast() && findPrevUserEntry();
    }

    @Override
    protected boolean internalSeek(Slice targetKey) {
        return iterator.seek(new InternalKey(targetKey, sequence, ValueType.VALUE)) && findNextUserEntry(false, null);
    }

    @Override
    protected boolean internalNext(boolean switchDirection) {
        if (switchDirection) {
            // iterator is pointing just before the entries for this.key(),
            // so advance into the range of entries for this.key() and then
            // use the normal skipping code below.
            boolean itrValid = iterator.valid();
            if (!itrValid) {
                itrValid = iterator.seekToFirst();
            } else {
                itrValid = iterator.next();
            }
            boolean valid = itrValid ? iterator.next() : iterator.seekToFirst();
            if (!valid) {
                this.key = null;
                this.value = null;
                return false;
            }
        }
        // find the next user entry after the key we are about to return
        return findNextUserEntry(true, this.key);
    }

    @Override
    protected boolean internalPrev(boolean switchDirection) {
        if (switchDirection) {
            Preconditions.checkState(iterator.valid(), "Should be valid");
            do {
                if (!iterator.prev()) {
                    return false;
                }
            } while (userComparator.compare(iterator.key().getUserKey(), this.key) >= 0);
        }
        return findPrevUserEntry();
    }

    @Override
    protected Slice internalKey() {
        return key;
    }

    private boolean findPrevUserEntry() {
        ValueType valueType = ValueType.DELETION;
        if (!iterator.valid()) {
            return false;
        }
        do {
            InternalKey key = iterator.key();
            if (key.getSequenceNumber() <= sequence) {
                if (valueType != ValueType.DELETION && userComparator.compare(key.getUserKey(), this.key) < 0) {
                    // We encountered a non-deleted value in entries for previous keys,
                    return true;
                }
                valueType = key.getValueType();
                if (valueType == ValueType.DELETION) {
                    this.key = null;
                    this.value = null;
                } else {
                    this.key = key.getUserKey();
                    this.value = iterator.value();
                }
            }
        } while (iterator.prev());
        if (valueType == ValueType.DELETION) {
            this.key = null;
            this.value = null;
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected Slice internalValue() {
        return value;
    }

    private boolean findNextUserEntry(boolean skipping, Slice savedKey) {
        // Loop until we hit an acceptable entry to yield
        if (!iterator.valid()) {
            return false;
        }
        do {
            InternalKey ikey = iterator.key();
            Slice value = iterator.value();
            listener.record(ikey, ikey.size() + value.length());
            if (ikey.getSequenceNumber() <= sequence) {
                switch (ikey.getValueType()) {
                    case DELETION:
                        // Arrange to skip all upcoming entries for this key since
                        // they are hidden by this deletion.
                        savedKey = ikey.getUserKey();
                        skipping = true;
                        break;
                    case VALUE:
                        if (skipping &&
                                userComparator.compare(ikey.getUserKey(), savedKey) <= 0) {
                            // Entry hidden
                        } else {
                            this.key = ikey.getUserKey();
                            this.value = value;
                            return true;
                        }
                        break;
                }
            }
        } while (iterator.next());
        this.key = null;
        this.value = null;
        return false;
    }

    @Override
    public String toString() {
        return "SnapshotSeekingIterator" +
                "{sequence=" + sequence +
                ", iterator=" + iterator +
                '}';
    }

    public interface IRecordBytesListener {
        void record(InternalKey internalKey, int bytes);
    }
}
