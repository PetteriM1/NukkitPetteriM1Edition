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
package org.iq80.leveldb.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * A wrapper for java based ZLib
 */
public final class ZLib {
    private static final ThreadLocal<Inflater> INFLATER = ThreadLocal.withInitial(Inflater::new);
    private static final ThreadLocal<Inflater> INFLATER_RAW = ThreadLocal.withInitial(() -> new Inflater(true));
    private static final ThreadLocal<Deflater> DEFLATER = ThreadLocal.withInitial(Deflater::new);
    private static final ThreadLocal<Deflater> DEFLATER_RAW = ThreadLocal.withInitial(() -> new Deflater(7, true));

    private ZLib() {
    }

    public static int compress(byte[] input, int inputOffset, int length, byte[] output, int outputOffset, boolean raw)
            throws IOException {
        Deflater deflater = (raw ? DEFLATER_RAW : DEFLATER).get();
        try {
            deflater.setInput(input, inputOffset, length);
            deflater.finish();

            return deflater.deflate(output, outputOffset, output.length - outputOffset);
        } finally {
            deflater.reset();
        }
    }

    public static ByteBuffer uncompress(ByteBuffer compressed, boolean raw) throws IOException {
        Inflater inflater = (raw ? INFLATER_RAW : INFLATER).get();

        try {
            ByteBuffer buffer = ByteBuffer.allocate(Math.max(1024, compressed.remaining() * 2));
            byte[] data = new byte[compressed.remaining()];
            compressed.get(data);
            inflater.setInput(data);

            while (!inflater.finished()) {
                try {
                    int bytesInflated = inflater.inflate(buffer.array(), buffer.position(), buffer.remaining());
                    buffer.position(buffer.position() + bytesInflated);

                    if (bytesInflated == 0 && inflater.needsInput()) {
                        throw new IOException("Inflater needs more input");
                    }
                } catch (DataFormatException e) {
                    throw new IOException("Could not decompress data", e);
                }

                if (!inflater.finished() && buffer.remaining() == 0) {
                    // Grow buffer
                    ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() + Math.max(1024, (compressed.remaining() * 2)));
                    buffer.flip();
                    newBuffer.put(buffer);
                    buffer = newBuffer;
                }
            }

            buffer.flip();
            return buffer;
        } finally {
            inflater.reset();
        }
    }
}
