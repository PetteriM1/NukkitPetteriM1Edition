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
package org.iq80.oldleveldb.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.zip.*;

/**
 * Some glue code that uses the java.util.zip classes to implement ZLIB
 * compression for leveldb.
 */
public class Zlib {

    static final private ZLibSPI ZLIB;
    static final private ZLibSPI ZLIB_RAW;
    private static final ThreadLocal<byte[]> BUFFER = ThreadLocal.withInitial(() -> new byte[32768]);

    static {
        ZLIB = new ZLibSPI(false);
        ZLIB_RAW = new ZLibSPI(true);
    }

    public static class ByteBufferBackedOutputStream extends OutputStream {
        final ByteBuffer buf;

        public ByteBufferBackedOutputStream(ByteBuffer buf) {
            this.buf = buf;
        }

        public void write(int b) throws IOException {
            buf.put((byte) b);
        }

        public void write(byte[] bytes, int off, int len) throws IOException {
            buf.put(bytes, off, len);
        }
    }

    /**
     * Use the same SPI interface as Snappy, for the case if leveldb ever gets
     * a compression plug-in type.
     */
    private static class ZLibSPI {

        private final ThreadLocal<Deflater> deflaterThreadLocal = new ThreadLocal<>();
        private final boolean raw;

        public ZLibSPI(boolean raw) {
            this.raw = raw;
        }

        public int compress(byte[] input, int inputOffset, int length,
                            byte[] output, int outputOffset) throws IOException {
            Deflater deflater = this.deflaterThreadLocal.get();
            if (deflater == null) {
                deflater = new Deflater(7, this.raw);
                this.deflaterThreadLocal.set(deflater);
            }

            deflater.reset();

            // TODO: parameters of Deflater to match MCPE expectations.
            ByteBufferBackedOutputStream stream = new ByteBufferBackedOutputStream(ByteBuffer.wrap(output,
                    outputOffset, output.length - outputOffset));

            return copy(
                    new DeflaterInputStream(new ByteArrayInputStream(input, inputOffset,
                            length), deflater),
                    stream
            );
        }

        private int copy(InputStream in, OutputStream out) throws IOException {
            byte[] buffer = BUFFER.get();
            int read;
            int count = 0;

            while (-1 != (read = in.read(buffer))) {
                out.write(buffer, 0, read);
                count += read;
            }

            return count;
        }
    }

    public static boolean available() {
        return ZLIB != null && ZLIB_RAW != null;
    }

    public static int compress(byte[] input, int inputOffset, int length,
                               byte[] output, int outputOffset) throws IOException {
        return ZLIB.compress(input, inputOffset, length, output, outputOffset);
    }

    public static int compressRaw(byte[] input, int inputOffset, int length,
                                  byte[] output, int outputOffset) throws IOException {
        return ZLIB_RAW.compress(input, inputOffset, length, output, outputOffset);
    }
}
