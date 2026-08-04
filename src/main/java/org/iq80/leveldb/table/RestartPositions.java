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

import org.iq80.leveldb.util.Slice;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkPositionIndex;
import static org.iq80.leveldb.util.SizeOf.SIZE_OF_INT;

final class RestartPositions {
    private final Slice restartPositions;
    private final int size;

    RestartPositions(Slice restartPositions) {
        Objects.requireNonNull(restartPositions);
        checkArgument(restartPositions.length() % SIZE_OF_INT == 0, "restartPositions.readableBytes() must be a multiple of %s", SIZE_OF_INT);
        this.restartPositions = restartPositions;
        this.size = restartPositions.length() / SIZE_OF_INT;
    }

    public int get(int index) {
        checkPositionIndex(index, size, "index out of range");
        return restartPositions.getInt(index * SIZE_OF_INT);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
