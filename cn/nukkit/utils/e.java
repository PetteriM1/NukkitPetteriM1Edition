/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import java.io.IOException;

interface e {
    public byte[] deflate(byte[][] var1, int var2) throws IOException;

    public byte[] deflate(byte[] var1, int var2) throws IOException;

    public byte[] deflateRaw(byte[][] var1, int var2) throws IOException;

    public byte[] deflateRaw(byte[] var1, int var2) throws IOException;

    public byte[] inflate(byte[] var1, int var2) throws IOException;

    public byte[] inflateRaw(byte[] var1, int var2) throws IOException;
}

