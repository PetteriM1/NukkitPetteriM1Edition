/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

public class Hash {
    public static long hashBlock(int n, int n2, int n3) {
        return (long)n2 + (((long)n & 0x3FFFFFFL) << 8) + (((long)n3 & 0x3FFFFFFL) << 34);
    }

    public static final int hashBlockX(long l) {
        return (int)((l >> 8 & 0x3FFFFFFL) << 38 >> 38);
    }

    public static final int hashBlockY(long l) {
        return (int)(l & 0xFFL);
    }

    public static final int hashBlockZ(long l) {
        return (int)((l >> 34 & 0x3FFFFFFL) << 38 >> 38);
    }
}

