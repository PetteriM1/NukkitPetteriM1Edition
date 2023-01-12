/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil.palette;

import java.util.Arrays;

public class IntPalette {
    private static final int[] b = new int[0];
    private int[] a = b;
    private int c = Integer.MIN_VALUE;

    public void add(int n) {
        this.a = this.a(n);
        this.c = Integer.MIN_VALUE;
    }

    protected void set(int[] nArray) {
        this.a = nArray;
        this.c = Integer.MIN_VALUE;
    }

    private int[] a(int n) {
        this.c = Integer.MIN_VALUE;
        if (this.a.length == 0) {
            return new int[]{n};
        }
        if (n < this.a[0]) {
            int[] nArray = new int[this.a.length + 1];
            System.arraycopy(this.a, 0, nArray, 1, this.a.length);
            nArray[0] = n;
            return nArray;
        }
        if (n > this.a[this.a.length - 1]) {
            int[] nArray = Arrays.copyOf(this.a, this.a.length + 1);
            nArray[this.a.length] = n;
            return nArray;
        }
        int[] nArray = Arrays.copyOf(this.a, this.a.length + 1);
        for (int k = 0; k < nArray.length; ++k) {
            if (this.a[k] < n) continue;
            System.arraycopy(this.a, k, nArray, k + 1, nArray.length - k - 1);
            nArray[k] = n;
            break;
        }
        return nArray;
    }

    public int getKey(int n) {
        return this.a[n];
    }

    public int getValue(int n) {
        int n2;
        boolean bl;
        int n3 = this.c;
        boolean bl2 = bl = n3 != Integer.MIN_VALUE;
        if (bl) {
            int n4 = this.a[n3];
            if (n4 == n) {
                return n3;
            }
            n2 = n4 > n ? this.a(0, n3, n) : this.a(n3 + 1, this.a.length, n);
        } else {
            n2 = this.a(0, this.a.length, n);
        }
        if (n2 >= this.a.length || n2 < 0) {
            this.c = Integer.MIN_VALUE;
            return Integer.MIN_VALUE;
        }
        this.c = n2;
        return this.c;
    }

    private int a(int n, int n2, int n3) {
        int n4 = n;
        int n5 = n2 - 1;
        while (n4 <= n5) {
            int n6 = n4 + n5 >>> 1;
            int n7 = this.a[n6];
            if (n7 < n3) {
                n4 = n6 + 1;
                continue;
            }
            if (n7 > n3) {
                n5 = n6 - 1;
                continue;
            }
            return n6;
        }
        return -(n4 + 1);
    }

    public int length() {
        return this.a.length;
    }

    public IntPalette clone() {
        IntPalette intPalette = new IntPalette();
        intPalette.a = this.a != b ? (int[])this.a.clone() : b;
        intPalette.c = this.c;
        return intPalette;
    }
}

