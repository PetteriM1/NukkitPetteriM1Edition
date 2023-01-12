/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil.palette;

import cn.nukkit.level.format.anvil.palette.BitArray256;
import cn.nukkit.level.format.anvil.palette.IntPalette;
import cn.nukkit.math.MathHelper;
import cn.nukkit.utils.ThreadCache;
import java.util.Arrays;

public final class BiomePalette {
    private int c;
    private BitArray256 a;
    private IntPalette b;

    private BiomePalette(BiomePalette biomePalette) {
        this.c = biomePalette.c;
        if (biomePalette.a != null) {
            this.a = biomePalette.a.clone();
            this.b = biomePalette.b.clone();
        }
    }

    public BiomePalette(int[] nArray) {
        for (int k = 0; k < 256; ++k) {
            this.set(k, nArray[k]);
        }
    }

    public BiomePalette() {
        this.c = Integer.MIN_VALUE;
    }

    public int get(int n, int n2) {
        return this.get(this.getIndex(n, n2));
    }

    public synchronized int get(int n) {
        if (this.a == null) {
            return this.c;
        }
        return this.b.getKey(this.a.getAt(n));
    }

    public void set(int n, int n2, int n3) {
        this.set(this.getIndex(n, n2), n3);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized void set(int n, int n2) {
        if (this.a == null) {
            if (n2 == this.c) {
                return;
            }
            if (this.c == Integer.MIN_VALUE) {
                this.c = n2;
                return;
            }
            BiomePalette biomePalette = this;
            synchronized (biomePalette) {
                this.b = new IntPalette();
                this.b.add(this.c);
                this.b.add(n2);
                this.a = new BitArray256(1);
                if (n2 < this.c) {
                    Arrays.fill(this.a.data, -1L);
                    this.a.setAt(n, 0);
                } else {
                    this.a.setAt(n, 1);
                }
                return;
            }
        }
        int n3 = this.b.getValue(n2);
        if (n3 != Integer.MIN_VALUE) {
            this.a.setAt(n, n3);
        } else {
            BiomePalette biomePalette = this;
            synchronized (biomePalette) {
                int n4;
                int[] nArray = this.a.toRaw(ThreadCache.intCache256.get());
                for (n4 = 0; n4 < 256; ++n4) {
                    nArray[n4] = this.b.getKey(nArray[n4]);
                }
                nArray[n] = n2;
                this.b.add(n2);
                n4 = MathHelper.log2(this.b.length() - 1);
                if (MathHelper.log2(this.b.length() - 2) != n4) {
                    this.a = new BitArray256(n4);
                }
                for (int k = 0; k < nArray.length; ++k) {
                    nArray[k] = this.b.getValue(nArray[k]);
                }
                this.a.fromRaw(nArray);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized int[] toRaw() {
        int[] nArray = ThreadCache.intCache256.get();
        if (this.a == null) {
            Arrays.fill(nArray, this.c);
        } else {
            BiomePalette biomePalette = this;
            synchronized (biomePalette) {
                nArray = this.a.toRaw(nArray);
                for (int k = 0; k < 256; ++k) {
                    nArray[k] = this.b.getKey(nArray[k]);
                }
            }
        }
        return nArray;
    }

    public int getIndex(int n, int n2) {
        return n2 << 4 | n;
    }

    public synchronized BiomePalette clone() {
        return new BiomePalette(this);
    }
}

