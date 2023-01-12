/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network;

import cn.nukkit.network.CompressionProvider;
import cn.nukkit.utils.BinaryStream;

final class b
implements CompressionProvider {
    b() {
    }

    @Override
    public byte[] compress(BinaryStream binaryStream, int n) throws Exception {
        return binaryStream.getBuffer();
    }

    @Override
    public byte[] decompress(byte[] byArray) throws Exception {
        return byArray;
    }
}

