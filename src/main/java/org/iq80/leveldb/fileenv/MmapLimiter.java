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
package org.iq80.leveldb.fileenv;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Helper class to limit mmap file usage so that we do not end up
 * running out virtual memory or running into kernel performance
 * problems for very large databases.
 */
public final class MmapLimiter {
    public static final int CPU_DATA_MODEL;

    static {
        boolean is64bit;
        if (System.getProperty("os.name").contains("Windows")) {
            is64bit = System.getenv("ProgramFiles(x86)") != null;
        } else {
            is64bit = System.getProperty("os.arch").contains("64");
        }
        CPU_DATA_MODEL = is64bit ? 64 : 32;
    }

    /**
     * We only use MMAP on 64 bit systems since it's really easy to run out of
     * virtual address space on a 32 bit system when all the data is getting mapped
     * into memory.  If you really want to use MMAP anyways, use -Dleveldb.mmap=true
     */
    public static final boolean USE_MMAP = Boolean.parseBoolean(System.getProperty("leveldb.mmap", String.valueOf(CPU_DATA_MODEL > 32)));

    private final AtomicInteger maxAllowedMmap;

    private MmapLimiter(int maxAllowedMmap) {
        this.maxAllowedMmap = new AtomicInteger(maxAllowedMmap);
    }

    /**
     * If another mmap slot is available, acquire it and return true.
     * Else return false.
     */
    public boolean acquire() {
        return maxAllowedMmap.getAndDecrement() > 0;
    }

    /**
     * Up to 1000 mmaps for 64-bit JVM; none for 32bit.
     */
    public static MmapLimiter defaultLimiter() {
        return new MmapLimiter(USE_MMAP ? 1000 : 0);
    }

    public static MmapLimiter newLimiter(int maxAllowedMmap) {
        return new MmapLimiter(maxAllowedMmap);
    }

    /**
     * Release a slot acquired by a previous call to Acquire() that returned true.
     */
    public void release() {
        maxAllowedMmap.incrementAndGet();
    }
}
