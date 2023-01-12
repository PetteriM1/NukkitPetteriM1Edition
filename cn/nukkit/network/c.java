/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network;

import cn.nukkit.network.CompressionProvider;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Zlib;

final class c
implements CompressionProvider {
    c() {
    }

    @Override
    public byte[] compress(BinaryStream binaryStream, int n) throws Exception {
        return Zlib.deflateRaw(binaryStream.getBuffer(), n);
    }

    @Override
    public byte[] decompress(byte[] byArray) throws Exception {
        return Zlib.inflateRaw(byArray, 0x200000);
    }
}

