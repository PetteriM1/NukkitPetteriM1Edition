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

import org.iq80.leveldb.CompressionType;
import org.iq80.oldleveldb.util.Slice;
import org.iq80.oldleveldb.util.SliceInput;
import org.iq80.oldleveldb.util.SliceOutput;
import org.iq80.oldleveldb.util.Slices;

public class BlockTrailer {
    public static final int ENCODED_LENGTH = 5;

    private final CompressionType compressionType;
    private final int crc32c;

    public BlockTrailer(CompressionType compressionType, int crc32c) {
        if (compressionType == null) throw new NullPointerException("compressionType is null");

        this.compressionType = compressionType;
        this.crc32c = crc32c;
    }

    public CompressionType getCompressionType() {
        return compressionType;
    }

    public int getCrc32c() {
        return crc32c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BlockTrailer that = (BlockTrailer) o;

        if (crc32c != that.crc32c) {
            return false;
        }
        if (compressionType != that.compressionType) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = compressionType.hashCode();
        result = 31 * result + crc32c;
        return result;
    }

    public static BlockTrailer readBlockTrailer(Slice slice) {
        SliceInput sliceInput = slice.input();
        CompressionType compressionType = CompressionType.getCompressionTypeByPersistentId(sliceInput.readUnsignedByte());
        int crc32c = sliceInput.readInt();
        return new BlockTrailer(compressionType, crc32c);
    }

    @Override
    public String toString() {
        return "BlockTrailer" +
                "{compressionType=" + compressionType +
                ", crc32c=0x" + Integer.toHexString(crc32c) +
                '}';
    }

    public static void writeBlockTrailer(BlockTrailer blockTrailer, SliceOutput sliceOutput) {
        sliceOutput.writeByte(blockTrailer.getCompressionType().persistentId());
        sliceOutput.writeInt(blockTrailer.getCrc32c());
    }

    public static Slice writeBlockTrailer(BlockTrailer blockTrailer) {
        Slice slice = Slices.allocate(ENCODED_LENGTH);
        writeBlockTrailer(blockTrailer, slice.output());
        return slice;
    }
}
