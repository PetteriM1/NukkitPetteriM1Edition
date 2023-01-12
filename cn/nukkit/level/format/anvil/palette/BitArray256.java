/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil.palette;

import cn.nukkit.utils.ThreadCache;

public final class BitArray256 {
    private final int a;
    protected final long[] data;

    public BitArray256(int n) {
        this.a = n;
        int n2 = this.a << 8 >> 6;
        this.data = new long[n2];
    }

    public BitArray256(BitArray256 bitArray256) {
        this.a = bitArray256.a;
        this.data = (long[])bitArray256.data.clone();
    }

    public final void setAt(int n, int n2) {
        int n3 = n * this.a;
        int n4 = n3 >> 6;
        int n5 = n3 & 0x3F;
        this.data[n4] = this.data[n4] & ((long)((1 << this.a) - 1) << n5 ^ 0xFFFFFFFFFFFFFFFFL) | (long)n2 << n5;
        if (n5 > 64 - this.a) {
            int n6 = n4 + 1;
            int n7 = 64 - n5;
            int n8 = this.a - n7;
            this.data[n6] = this.data[n6] >>> n8 << n8 | (long)n2 >> n7;
        }
    }

    public final int getAt(int n) {
        int n2 = n * this.a;
        int n3 = n2 >> 6;
        int n4 = n2 & 0x3F;
        if (n4 <= 64 - this.a) {
            return (int)(this.data[n3] >>> n4 & (long)((1 << this.a) - 1));
        }
        return (int)((this.data[n3] >>> n4 | this.data[n3 + 1] << 64 - n4) & (long)((1 << this.a) - 1));
    }

    public final void fromRaw(int[] nArray) {
        for (int k = 0; k < nArray.length; ++k) {
            this.setAt(k, nArray[k]);
        }
    }

    public BitArray256 grow(int n) {
        if (n - this.a <= 0) {
            return this;
        }
        BitArray256 bitArray256 = new BitArray256(n);
        int[] nArray = ThreadCache.intCache256.get();
        this.toRaw(nArray);
        bitArray256.fromRaw(nArray);
        return bitArray256;
    }

    public BitArray256 growSlow(int n) {
        BitArray256 bitArray256 = new BitArray256(n);
        for (int k = 0; k < 256; ++k) {
            bitArray256.setAt(k, this.getAt(k));
        }
        return bitArray256;
    }

    public final int[] toRaw(int[] nArray) {
        for (int k = 0; k < nArray.length; ++k) {
            nArray[k] = this.getAt(k);
        }
        return nArray;
    }

    public final int[] toRaw() {
        return this.toRaw(new int[256]);
    }

    public BitArray256 clone() {
        return new BitArray256(this);
    }
}

