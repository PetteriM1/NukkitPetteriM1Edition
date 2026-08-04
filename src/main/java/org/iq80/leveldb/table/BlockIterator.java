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
package org.iq80.leveldb.table;

import org.iq80.leveldb.iterator.ASeekingIterator;
import org.iq80.leveldb.iterator.SliceIterator;
import org.iq80.leveldb.util.*;

import java.util.Comparator;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public final class BlockIterator extends ASeekingIterator<Slice, Slice>
        implements SliceIterator {
    private final SliceInput data;
    private final RestartPositions restartPositions;
    private final Comparator<Slice> comparator;

    private int current;
    private int restartIndex;
    private Slice key;
    private Slice value;

    public BlockIterator(Slice data, Slice restartPositions, Comparator<Slice> comparator) {
        if (data == null) throw new NullPointerException("data is null");
        if (restartPositions == null) throw new NullPointerException("restartPositions is null");
        if (comparator == null) throw new NullPointerException("comparator is null");

        this.data = Objects.requireNonNull(data.input());

        this.restartPositions = new RestartPositions(restartPositions);
        checkArgument(!this.restartPositions.isEmpty(),
                "At least one restart position is expected");
        this.comparator = comparator;
    }

    @Override
    protected Slice internalKey() {
        return key;
    }

    @Override
    protected Slice internalValue() {
        return value;
    }

    @Override
    protected boolean internalNext(boolean switchDirection) {
        return parseNextKey();
    }

    @Override
    protected boolean internalPrev(boolean switchDirection) {
        // Scan backwards to a restart point before current
        final int original = current;
        while (restartPositions.get(restartIndex) >= original) {
            if (restartIndex == 0) {
                current = Integer.MAX_VALUE;
                return false;
            }
            restartIndex--;
        }

        seekToRestartPoint(restartIndex);
        do {
            // Loop until end of current entry hits the start of original entry
        } while (parseNextKey() && data.position() < original);
        return valid();
    }

    private void seekToRestartPoint(int index) {
        this.restartIndex = index;
        this.data.setPosition(restartPositions.get(restartIndex));
        this.key = null;
        this.value = null;
    }

    /**
     * Repositions the iterator so the beginning of this block.
     */
    @Override
    protected boolean internalSeekToFirst() {
        seekToRestartPosition(0);
        return parseNextKey();
    }

    protected boolean internalSeekToLast() {
        seekToRestartPoint(restartPositions.size() - 1); // we have at lease one restart
        boolean valid;
        do {
            valid = parseNextKey();
        } while (valid && data.isReadable());
        return valid;
    }

    /**
     * Repositions the iterator so the key of the next BlockElement returned greater than or equal to the specified targetKey.
     */
    @Override
    protected boolean internalSeek(Slice targetKey) {
        int left = 0;
        int right = restartPositions.size() - 1;

        // binary search restart positions to find the restart position immediately before the targetKey
        while (left < right) {
            int mid = (left + right + 1) >> 1;

            seekToRestartPosition(mid);

            Slice key = readFirstKeyAtRestartPoint();

            if (comparator.compare(key, targetKey) < 0) {
                // key at mid is smaller than targetKey.  Therefore all restart
                // blocks before mid are uninteresting.
                left = mid;
            } else {
                // key at mid is greater than or equal to targetKey.  Therefore
                // all restart blocks at or after mid are uninteresting.
                right = mid - 1;
            }
        }

        // linear search (within restart block) for first key greater than or equal to targetKey
        seekToRestartPosition(left);
        while (parseNextKey()) { //load this.key
            if (comparator.compare(key, targetKey) >= 0) {
                return true;
            }
        }
        current = data.position();
        return false;
    }

    /**
     * Seeks to and reads the entry at the specified restart position.
     * <p/>
     * After this method, nextEntry will contain the next entry to return, and the previousEntry will be null.
     */
    private void seekToRestartPosition(int restartPosition) {
        // seek data readIndex to the beginning of the restart block
        this.restartIndex = restartPosition;
        this.key = null;
        this.value = null;
        int offset = restartPositions.get(restartPosition);
        data.setPosition(offset);
        current = offset;
    }

    private Slice readFirstKeyAtRestartPoint() {
        checkState(VariableLengthQuantity.readVariableLengthInt(data) == 0,
                "First restart position can't have a shared ");
        current = data.position();
        int nonSharedKeyLength = VariableLengthQuantity.readVariableLengthInt(data);
        //data size
        VariableLengthQuantity.readVariableLengthInt(data);
        return data.readSlice(nonSharedKeyLength);
    }

    /**
     * Reads the entry at the current data readIndex.
     * After this method, data readIndex is positioned at the beginning of the next entry
     * or at the end of data if there was not a next entry.
     *
     * @return true if an entry was read
     */
    private boolean parseNextKey() {
        current = data.position();
        if (!data.isReadable()) {
            return false;
        }
        // read entry header
        int sharedKeyLength = VariableLengthQuantity.readVariableLengthInt(data);
        int nonSharedKeyLength = VariableLengthQuantity.readVariableLengthInt(data);
        int valueLength = VariableLengthQuantity.readVariableLengthInt(data);

        // read key
        Slice key;
        if (sharedKeyLength > 0) {
            key = Slices.allocate(sharedKeyLength + nonSharedKeyLength);
            SliceOutput sliceOutput = key.output();
            checkState(this.key != null, "Entry has a shared key but no previous entry was provided");
            sliceOutput.writeBytes(this.key, 0, sharedKeyLength);
            sliceOutput.writeBytes(data, nonSharedKeyLength);
        } else {
            key = data.readSlice(nonSharedKeyLength);
        }
        // read value
        Slice value = data.readSlice(valueLength);

        this.key = key;
        this.value = value;
        return true;
    }

    @Override
    protected void internalClose() {
        //na
    }
}
