/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil.palette;

import cn.nukkit.utils.ThreadCache;

public final class BitArray {
    private final int c;
    private final int d;
    private final int b;
    private final long[] a;

    public BitArray(int n) {
        this.c = n;
        this.d = 64 - n;
        this.b = (1 << n) - 1;
        this.a = new long[this.c << 12 >> 6];
    }

    public final void setAt(int n, int n2) {
        int n3 = n * this.c;
        int n4 = n3 >> 6;
        int n5 = n3 & 0x3F;
        this.a[n4] = this.a[n4] & ((long)this.b << n5 ^ 0xFFFFFFFFFFFFFFFFL) | (long)n2 << n5;
        if (n5 > this.d) {
            int n6 = n4 + 1;
            int n7 = 64 - n5;
            int n8 = this.c - n7;
            this.a[n6] = this.a[n6] >>> n8 << n8 | (long)n2 >> n7;
        }
    }

    public final int getAt(int n) {
        int n2 = n * this.c;
        int n3 = n2 >> 6;
        int n4 = n2 & 0x3F;
        if (n4 <= this.d) {
            return (int)(this.a[n3] >>> n4 & (long)this.b);
        }
        return (int)((this.a[n3] >>> n4 | this.a[n3 + 1] << 64 - n4) & (long)this.b);
    }

    public final void fromRawSlow(char[] cArray) {
        for (int k = 0; k < cArray.length; ++k) {
            this.setAt(k, cArray[k]);
        }
    }

    public final void fromRaw(char[] cArray) {
        long[] lArray = this.a;
        int n = lArray.length;
        int n2 = this.c;
        int n3 = this.d;
        int n4 = 0;
        int n5 = 0;
        long l = 0L;
        for (int k = 0; k < n; ++k) {
            char c2;
            while (n4 <= n3) {
                c2 = cArray[n5++];
                l |= (long)c2 << n4;
                n4 += n2;
            }
            if (n4 < 64) {
                if (k == n - 1) continue;
                c2 = cArray[n5++];
                int n6 = 64 - n4;
                long l2 = c2 >> n6;
                lArray[k] = l |= (long)c2 - (l2 << n6) << n4;
                lArray[k + 1] = l = l2;
                n4 -= n3;
                continue;
            }
            n4 = 0;
            lArray[k] = l;
            l = 0L;
        }
    }

    public BitArray grow(int n) {
        if (n - this.c <= 0) {
            return this;
        }
        BitArray bitArray = new BitArray(n);
        char[] cArray = ThreadCache.charCache4096.get();
        this.toRaw(cArray);
        bitArray.fromRaw(cArray);
        return bitArray;
    }

    public BitArray growSlow(int n) {
        BitArray bitArray = new BitArray(n);
        for (int k = 0; k < 4096; ++k) {
            bitArray.setAt(k, this.getAt(k));
        }
        return bitArray;
    }

    public final char[] toRawSlow() {
        char[] cArray = new char[4096];
        for (int k = 0; k < cArray.length; ++k) {
            cArray[k] = (char)this.getAt(k);
        }
        return cArray;
    }

    public final char[] toRaw() {
        return this.toRaw(new char[4096]);
    }

    protected final char[] toRaw(char[] cArray) {
        long[] lArray = this.a;
        int n = lArray.length;
        int n2 = this.c;
        int n3 = this.b;
        int n4 = this.d;
        int n5 = 0;
        int n6 = 0;
        for (int k = 0; k < n; ++k) {
            char c2;
            long l = lArray[k];
            while (n5 <= n4) {
                c2 = (char)(l >>> n5 & (long)n3);
                cArray[n6++] = c2;
                n5 += n2;
            }
            if (n5 < 64) {
                if (k == n - 1) continue;
                c2 = (char)(l >>> n5);
                l = lArray[k + 1];
                c2 = (char)((long)c2 | l << n2 - (n5 -= n4));
                c2 = (char)(c2 & n3);
                cArray[n6++] = c2;
                continue;
            }
            n5 = 0;
        }
        return cArray;
    }
}

