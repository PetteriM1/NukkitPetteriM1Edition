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

import java.io.Closeable;
import java.io.IOException;

/**
 * An interface for writing log messages.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public interface Logger extends Closeable {
    @Override
    default void close() throws IOException {
        //default to be compatible with older interface
    }

    /**
     * Substitutes each {@code %s} in {@code template} with an argument. Arguments without place holder will
     * be placed at the end of template.
     * <p>
     * This is a default method to avoid incompatibilities with older logger interface.
     *
     * @param template a non-null template string containing 0 or more {@code %s} placeholders.
     * @param args     the arguments to be substituted into the message template.
     */
    default void log(String template, Object... args) {
        log(String.format(template, args));
    }

    void log(String message);
}
