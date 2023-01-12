/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil.palette;

import java.util.Arrays;

public class BytePalette {
    private static final byte[] a = new byte[0];
    private byte[] c = a;
    private byte b = (byte)-128;

    public void add(byte by) {
        this.c = this.a(by);
        this.b = (byte)-128;
    }

    protected void set(byte[] byArray) {
        this.c = byArray;
        this.b = (byte)-128;
    }

    private byte[] a(byte by) {
        this.b = (byte)-128;
        if (this.c.length == 0) {
            return new byte[]{by};
        }
        if (by < this.c[0]) {
            byte[] byArray = new byte[this.c.length + 1];
            System.arraycopy(this.c, 0, byArray, 1, this.c.length);
            byArray[0] = by;
            return byArray;
        }
        if (by > this.c[this.c.length - 1]) {
            byte[] byArray = Arrays.copyOf(this.c, this.c.length + 1);
            byArray[this.c.length] = by;
            return byArray;
        }
        byte[] byArray = Arrays.copyOf(this.c, this.c.length + 1);
        for (int k = 0; k < byArray.length; ++k) {
            if (this.c[k] < by) continue;
            System.arraycopy(this.c, k, byArray, k + 1, byArray.length - k - 1);
            byArray[k] = by;
            break;
        }
        return byArray;
    }

    public byte getKey(int n) {
        return this.c[n];
    }

    public byte getValue(byte by) {
        int n;
        boolean bl;
        byte by2 = this.b;
        boolean bl2 = bl = by2 != -128;
        if (bl) {
            byte by3 = this.c[by2];
            if (by3 == by) {
                return by2;
            }
            n = by3 > by ? this.a(0, by2, by) : this.a(by2 + 1, this.c.length, by);
        } else {
            n = this.a(0, this.c.length, by);
        }
        if (n >= this.c.length || n < 0) {
            this.b = -128;
            return -128;
        }
        this.b = (byte)n;
        return this.b;
    }

    private int a(int n, int n2, byte by) {
        int n3 = n;
        int n4 = n2 - 1;
        while (n3 <= n4) {
            int n5 = n3 + n4 >>> 1;
            byte by2 = this.c[n5];
            if (by2 < by) {
                n3 = n5 + 1;
                continue;
            }
            if (by2 > by) {
                n4 = n5 - 1;
                continue;
            }
            return n5;
        }
        return -(n3 + 1);
    }
}

