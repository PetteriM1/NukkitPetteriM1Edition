/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil.palette;

import java.util.Arrays;

public class CharPalette {
    private static final char[] b = new char[0];
    private char[] c = b;
    private char a = (char)65535;

    public void add(char c2) {
        this.c = this.a(c2);
        this.a = (char)65535;
    }

    protected void set(char[] cArray) {
        this.c = cArray;
        this.a = (char)65535;
    }

    private char[] a(char c2) {
        this.a = (char)65535;
        if (this.c.length == 0) {
            return new char[]{c2};
        }
        if (c2 < this.c[0]) {
            char[] cArray = new char[this.c.length + 1];
            System.arraycopy(this.c, 0, cArray, 1, this.c.length);
            cArray[0] = c2;
            return cArray;
        }
        if (c2 > this.c[this.c.length - 1]) {
            char[] cArray = Arrays.copyOf(this.c, this.c.length + 1);
            cArray[this.c.length] = c2;
            return cArray;
        }
        char[] cArray = Arrays.copyOf(this.c, this.c.length + 1);
        for (int k = 0; k < cArray.length; ++k) {
            if (this.c[k] < c2) continue;
            System.arraycopy(this.c, k, cArray, k + 1, cArray.length - k - 1);
            cArray[k] = c2;
            break;
        }
        return cArray;
    }

    public char getKey(int n) {
        return this.c[n];
    }

    public char getValue(char c2) {
        int n;
        boolean bl;
        char c3 = this.a;
        boolean bl2 = bl = c3 != '\uffff';
        if (bl) {
            char c4 = this.c[c3];
            if (c4 == c2) {
                return c3;
            }
            n = c4 > c2 ? this.a(0, c3, c2) : this.a(c3 + '\u0001', this.c.length, c2);
        } else {
            n = this.a(0, this.c.length, c2);
        }
        if (n >= this.c.length || n < 0) {
            this.a = '\uffff';
            return '\uffff';
        }
        this.a = (char)n;
        return this.a;
    }

    private int a(int n, int n2, char c2) {
        int n3 = n;
        int n4 = n2 - 1;
        while (n3 <= n4) {
            int n5 = n3 + n4 >>> 1;
            char c3 = this.c[n5];
            if (c3 < c2) {
                n3 = n5 + 1;
                continue;
            }
            if (c3 > c2) {
                n4 = n5 - 1;
                continue;
            }
            return n5;
        }
        return -(n3 + 1);
    }
}

