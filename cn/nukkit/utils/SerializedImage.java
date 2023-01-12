/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import java.util.Objects;

public class SerializedImage {
    public static final SerializedImage EMPTY = new SerializedImage(0, 0, new byte[0]);
    public final int width;
    public final int height;
    public final byte[] data;

    public SerializedImage(int n, int n2, byte[] byArray) {
        this.width = n;
        this.height = n2;
        this.data = byArray;
    }

    public static SerializedImage fromLegacy(byte[] byArray) {
        Objects.requireNonNull(byArray, "skinData");
        switch (byArray.length) {
            case 8192: {
                return new SerializedImage(64, 32, byArray);
            }
            case 16384: {
                return new SerializedImage(64, 64, byArray);
            }
            case 32768: {
                return new SerializedImage(128, 64, byArray);
            }
            case 65536: {
                return new SerializedImage(128, 128, byArray);
            }
        }
        throw new IllegalArgumentException("Unknown legacy skin size");
    }

    public String toString() {
        return "SerializedImage(width=" + this.width + ", height=" + this.height + ")";
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

