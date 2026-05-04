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
package org.iq80.oldleveldb.impl;

import com.google.common.base.Preconditions;
import org.iq80.leveldb.impl.ValueType;
import org.iq80.oldleveldb.util.Slice;
import org.iq80.oldleveldb.util.SliceOutput;
import org.iq80.oldleveldb.util.Slices;

import java.util.Objects;

import static com.google.common.base.Charsets.UTF_8;
import static org.iq80.leveldb.util.SizeOf.SIZE_OF_LONG;

public class InternalKey {
    private final Slice userKey;
    private final long sequenceNumber;
    private final ValueType valueType;
    private int hash;

    public InternalKey(Slice userKey, long sequenceNumber, ValueType valueType) {
        Preconditions.checkNotNull(userKey);
        Preconditions.checkArgument(sequenceNumber >= 0);
        Preconditions.checkNotNull(valueType);

        this.userKey = userKey;
        this.sequenceNumber = sequenceNumber;
        this.valueType = valueType;
    }

    public InternalKey(Slice data) {
        Preconditions.checkNotNull(data);
        Preconditions.checkArgument(data.length() >= SIZE_OF_LONG);
        this.userKey = getUserKey(data);
        long packedSequenceAndType = data.getLong(data.length() - SIZE_OF_LONG);
        this.sequenceNumber = SequenceNumber.unpackSequenceNumber(packedSequenceAndType);
        this.valueType = SequenceNumber.unpackValueType(packedSequenceAndType);
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public Slice getUserKey() {
        return userKey;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public Slice encode() {
        Slice slice = Slices.allocate(userKey.length() + SIZE_OF_LONG);
        SliceOutput sliceOutput = slice.output();
        sliceOutput.writeBytes(userKey);
        sliceOutput.writeLong(SequenceNumber.packSequenceAndValueType(sequenceNumber, valueType));
        return slice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InternalKey that = (InternalKey) o;

        if (sequenceNumber != that.sequenceNumber) {
            return false;
        }
        if (!Objects.equals(userKey, that.userKey)) {
            return false;
        }
        return valueType == that.valueType;
    }

    private static Slice getUserKey(Slice data) {
        return data.slice(0, data.length() - SIZE_OF_LONG);
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            int result = userKey != null ? userKey.hashCode() : 0;
            result = 31 * result + (int) (sequenceNumber ^ (sequenceNumber >>> 32));
            result = 31 * result + (valueType != null ? valueType.hashCode() : 0);
            if (result == 0) {
                result = 1;
            }
            hash = result;
        }
        return hash;
    }

    @Override
    public String toString() {
        // todo don't print the real value
        return "InternalKey" +
                "{key=" + getUserKey().toString(UTF_8) +      // todo don't print the real value
                ", sequenceNumber=" + getSequenceNumber() +
                ", valueType=" + getValueType() +
                '}';
    }
}
