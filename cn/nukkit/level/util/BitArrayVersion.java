/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.util;

import cn.nukkit.level.util.BitArray;
import cn.nukkit.level.util.PaddedBitArray;
import cn.nukkit.level.util.Pow2BitArray;

public enum BitArrayVersion {
    V16(16, 2, null),
    V8(8, 4, V16),
    V6(6, 5, V8),
    V5(5, 6, V6),
    V4(4, 8, V5),
    V3(3, 10, V4),
    V2(2, 16, V3),
    V1(1, 32, V2);

    final byte b;
    final byte e;
    final int a;
    final BitArrayVersion d;
    final int c;

    private BitArrayVersion(int n2, int n3, BitArrayVersion bitArrayVersion) {
        this.b = (byte)n2;
        this.e = (byte)n3;
        this.a = (1 << this.b) - 1;
        this.d = bitArrayVersion;
        this.c = this.getWordsForSize(4096);
    }

    public static BitArrayVersion get(int n, boolean bl) {
        for (BitArrayVersion bitArrayVersion : BitArrayVersion.values()) {
            if ((bl || bitArrayVersion.e > n) && (!bl || bitArrayVersion.b != n)) continue;
            return bitArrayVersion;
        }
        throw new IllegalArgumentException("Invalid palette version: " + n);
    }

    public BitArray createPalette() {
        return this.createPalette(4096, new int[this.c]);
    }

    public BitArray createPalette(int n) {
        return this.createPalette(n, new int[this.getWordsForSize(n)]);
    }

    public byte getId() {
        return this.b;
    }

    public int getWordsForSize(int n) {
        return n / this.e + (n % this.e == 0 ? 0 : 1);
    }

    public int getMaxEntryValue() {
        return this.a;
    }

    public BitArrayVersion next() {
        return this.d;
    }

    public BitArray createPalette(int n, int[] nArray) {
        if (this == V3 || this == V5 || this == V6) {
            return new PaddedBitArray(this, n, nArray);
        }
        return new Pow2BitArray(this, n, nArray);
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

