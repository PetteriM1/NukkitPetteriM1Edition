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
package org.iq80.leveldb;

/**
 * DB contents are stored in a set of blocks, each of which holds a
 * sequence of key,value pairs.  Each block may be compressed before
 * being stored in a file.  The following enum describes which
 * compression method (if any) is used to compress a block.
 */
public enum CompressionType {
    // NOTE: do not change the values of existing entries, as these are
    // part of the persistent format on disk.
    NONE(0x00),
    SNAPPY(0x01),
    // MCPE Compression Types
    ZLIB(0x02),
    ZLIB_RAW(0x04);

    private final int persistentId;

    CompressionType(int persistentId) {
        this.persistentId = persistentId;
    }

    public static CompressionType getCompressionTypeByPersistentId(int persistentId) {
        for (CompressionType compressionType : CompressionType.values()) {
            if (compressionType.persistentId == persistentId) {
                return compressionType;
            }
        }
        throw new IllegalArgumentException("Unknown persistentId " + persistentId);
    }

    public int persistentId() {
        return persistentId;
    }
}
