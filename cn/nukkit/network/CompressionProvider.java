/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network;

import cn.nukkit.network.a;
import cn.nukkit.network.b;
import cn.nukkit.network.c;
import cn.nukkit.network.protocol.types.PacketCompressionAlgorithm;
import cn.nukkit.utils.BinaryStream;

public interface CompressionProvider {
    public static final CompressionProvider NONE = new b();
    public static final CompressionProvider ZLIB = new a();
    public static final CompressionProvider ZLIB_RAW = new c();

    public byte[] compress(BinaryStream var1, int var2) throws Exception;

    public byte[] decompress(byte[] var1) throws Exception;

    public static CompressionProvider from(PacketCompressionAlgorithm packetCompressionAlgorithm, int n) {
        if (packetCompressionAlgorithm == null) {
            return NONE;
        }
        if (packetCompressionAlgorithm == PacketCompressionAlgorithm.ZLIB) {
            return n < 10 ? ZLIB : ZLIB_RAW;
        }
        throw new UnsupportedOperationException();
    }

    private static UnsupportedOperationException a(UnsupportedOperationException unsupportedOperationException) {
        return unsupportedOperationException;
    }
}

