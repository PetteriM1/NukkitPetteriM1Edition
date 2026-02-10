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
package org.iq80.oldleveldb.table;

import org.iq80.oldleveldb.util.*;

public class BlockHandle {
    public static final int MAX_ENCODED_LENGTH = 10 + 10;

    private final long offset;
    private final int dataSize;

    BlockHandle(long offset, int dataSize) {
        this.offset = offset;
        this.dataSize = dataSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BlockHandle that = (BlockHandle) o;

        if (dataSize != that.dataSize) {
            return false;
        }
        if (offset != that.offset) {
            return false;
        }

        return true;
    }

    public int getDataSize() {
        return dataSize;
    }

    public long getOffset() {
        return offset;
    }

    @Override
    public int hashCode() {
        int result = (int) (offset ^ (offset >>> 32));
        result = 31 * result + dataSize;
        return result;
    }

    public static BlockHandle readBlockHandle(SliceInput sliceInput) {
        long offset = VariableLengthQuantity.readVariableLengthLong(sliceInput);
        long size = VariableLengthQuantity.readVariableLengthLong(sliceInput);

        if (size > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Blocks can not be larger than Integer.MAX_VALUE");
        }

        return new BlockHandle(offset, (int) size);
    }

    @Override
    public String toString() {
        return "BlockHandle" +
                "{offset=" + offset +
                ", dataSize=" + dataSize +
                '}';
    }

    public static Slice writeBlockHandle(BlockHandle blockHandle) {
        Slice slice = Slices.allocate(MAX_ENCODED_LENGTH);
        SliceOutput sliceOutput = slice.output();
        writeBlockHandleTo(blockHandle, sliceOutput);
        return slice.slice();
    }

    public static void writeBlockHandleTo(BlockHandle blockHandle, SliceOutput sliceOutput) {
        VariableLengthQuantity.writeVariableLengthLong(blockHandle.offset, sliceOutput);
        VariableLengthQuantity.writeVariableLengthLong(blockHandle.dataSize, sliceOutput);
    }
}
