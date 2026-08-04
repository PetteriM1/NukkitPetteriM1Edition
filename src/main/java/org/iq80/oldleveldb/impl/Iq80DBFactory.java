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

import org.iq80.leveldb.Options;
import org.iq80.oldleveldb.DB;
import org.iq80.oldleveldb.DBFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class Iq80DBFactory
        implements DBFactory {
    public static final boolean IS_64_BIT = is64bit();
    // We only use MMAP on 64 bit systems since it's really easy to run out of
    // virtual address space on a 32 bit system when all the data is getting mapped
    // into memory.  If you really want to use MMAP anyways, use -Dleveldb.mmap=true
    public static final boolean USE_MMAP = Boolean.parseBoolean(System.getProperty("leveldb.mmap", Boolean.toString(IS_64_BIT)));
    public static final String VERSION = "0.11.1-SNAPSHOT (CloudburstMC)";
    public static final Iq80DBFactory factory = new Iq80DBFactory();

    private static boolean is64bit() {
        boolean is64bit;
        if (System.getProperty("os.name").contains("Windows")) {
            is64bit = System.getenv("ProgramFiles(x86)") != null;
        } else {
            is64bit = System.getProperty("os.arch").contains("64");
        }
        return is64bit;
    }

    @Override
    public DB open(File path, Options options) throws IOException {
        return new DbImpl(options, path);
    }

    @Override
    public String toString() {
        return String.format("iq80 leveldb version %s", VERSION);
    }
}
