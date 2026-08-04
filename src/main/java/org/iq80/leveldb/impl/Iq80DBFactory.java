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
package org.iq80.leveldb.impl;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.env.Env;
import org.iq80.leveldb.fileenv.EnvImpl;

import java.io.*;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class Iq80DBFactory
        implements DBFactory {
    public static final String VERSION = "unknown";

    public static final Iq80DBFactory factory = new Iq80DBFactory();

    @Override
    public DB open(File path, Options options)
            throws IOException {
        Objects.requireNonNull(path);
        return new DbImpl(options, path.getAbsolutePath(), EnvImpl.createEnv());
    }

    @Override
    public void destroy(File path, Options options)
            throws IOException {
        Objects.requireNonNull(path);
        Env env = EnvImpl.createEnv();
        DbImpl.destroyDB(env.toFile(path.getAbsolutePath()), env);
    }

    @Override
    public void repair(File path, Options options)
            throws IOException {
        // TODO: implement repair
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return String.format("iq80 leveldb version %s", VERSION);
    }

    public static byte[] bytes(String value) {
        return (value == null) ? null : value.getBytes(UTF_8);
    }

    public static String asString(byte[] value) {
        return (value == null) ? null : new String(value, UTF_8);
    }
}
