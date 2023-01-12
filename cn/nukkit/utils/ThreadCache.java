/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.nbt.stream.FastByteArrayOutputStream;
import cn.nukkit.utils.BinaryStream;
import java.util.BitSet;

public class ThreadCache {
    public static final ThreadLocal<byte[][]> idArray = ThreadLocal.withInitial(() -> new byte[16][]);
    public static final ThreadLocal<byte[][]> dataArray = ThreadLocal.withInitial(() -> new byte[16][]);
    public static final ThreadLocal<byte[]> byteCache6144 = ThreadLocal.withInitial(() -> new byte[6144]);
    public static final ThreadLocal<byte[]> byteCache256 = ThreadLocal.withInitial(() -> new byte[256]);
    public static final ThreadLocal<BitSet> boolCache4096 = ThreadLocal.withInitial(() -> new BitSet(4096));
    public static final ThreadLocal<char[]> charCache4096v2 = ThreadLocal.withInitial(() -> new char[4096]);
    public static final ThreadLocal<char[]> charCache4096 = ThreadLocal.withInitial(() -> new char[4096]);
    public static final ThreadLocal<int[]> intCache256 = ThreadLocal.withInitial(() -> new int[256]);
    public static final ThreadLocal<FastByteArrayOutputStream> fbaos = ThreadLocal.withInitial(() -> new FastByteArrayOutputStream(1024));
    public static final ThreadLocal<BinaryStream> binaryStream = ThreadLocal.withInitial(BinaryStream::new);

    public static void clean() {
    }
}

