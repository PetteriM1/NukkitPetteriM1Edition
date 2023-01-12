/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil.palette;

import cn.nukkit.Server;
import cn.nukkit.level.format.anvil.palette.BitArray4096;
import cn.nukkit.level.format.anvil.palette.CharPalette;
import cn.nukkit.math.MathHelper;
import cn.nukkit.utils.ThreadCache;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.BitSet;

public final class BlockDataPalette
implements Cloneable {
    private static final int a = 4096;
    private volatile char[] c;
    private volatile BitArray4096 d;
    private volatile CharPalette b;

    public BlockDataPalette() {
        this(new char[4096]);
    }

    public BlockDataPalette(char[] cArray) {
        Preconditions.checkArgument(cArray.length == 4096, "Data is not 4096");
        this.c = cArray;
    }

    private char[] a() {
        char[] cArray = this.c;
        if (cArray != null) {
            return cArray;
        }
        if (!Server.getInstance().isPrimaryThread()) {
            return this.getRaw();
        }
        return this.c;
    }

    public synchronized char[] getRaw() {
        CharPalette charPalette = this.b;
        BitArray4096 bitArray4096 = this.d;
        this.d = null;
        this.b = null;
        char[] cArray = this.c;
        if (cArray == null && charPalette != null) {
            cArray = bitArray4096 != null ? bitArray4096.toRaw() : new char[4096];
            for (int k = 0; k < 4096; ++k) {
                cArray[k] = charPalette.getKey(cArray[k]);
            }
        } else {
            cArray = new char[4096];
        }
        this.c = cArray;
        return this.c;
    }

    private int a(int n, int n2, int n3) {
        return (n << 8) + (n3 << 4) + n2;
    }

    public int getBlockData(int n, int n2, int n3) {
        return this.getFullBlock(n, n2, n3) & 0xF;
    }

    public int getBlockId(int n, int n2, int n3) {
        return this.getFullBlock(n, n2, n3) >> 4;
    }

    public void setBlockId(int n, int n2, int n3, int n4) {
        this.setFullBlock(n, n2, n3, (char)(n4 << 4));
    }

    public synchronized void setBlockData(int n, int n2, int n3, int n4) {
        char c2;
        int n5 = this.a(n, n2, n3);
        char[] cArray = this.a();
        if (cArray != null) {
            c2 = cArray[n5];
            cArray[n5] = (char)(c2 & 0xFFF0 | n4);
        }
        if (this.b != null && this.d != null) {
            c2 = this.b.getKey(this.d.getAt(n5));
            if ((c2 & 0xF) != n4) {
                this.b(n5, (char)(c2 & 0xFFF0 | n4));
            }
        } else {
            throw new IllegalStateException("Raw data and pallete was null");
        }
    }

    public int getFullBlock(int n, int n2, int n3) {
        return this.a(this.a(n, n2, n3));
    }

    public void setFullBlock(int n, int n2, int n3, int n4) {
        this.c(this.a(n, n2, n3), (char)n4);
    }

    public int getAndSetFullBlock(int n, int n2, int n3, int n4) {
        return this.a(this.a(n, n2, n3), (char)n4);
    }

    private int a(int n, char c2) {
        char[] cArray = this.a();
        if (cArray != null) {
            char c3 = cArray[n];
            cArray[n] = c2;
            return c3;
        }
        if (this.b != null && this.d != null) {
            char c4 = this.b.getKey(this.d.getAt(n));
            if (c4 != c2) {
                this.b(n, c2);
            }
            return c4;
        }
        throw new IllegalStateException("Raw data and pallete was null");
    }

    private int a(int n) {
        char[] cArray = this.a();
        if (cArray != null) {
            return cArray[n];
        }
        if (this.b != null && this.d != null) {
            return this.b.getKey(this.d.getAt(n));
        }
        throw new IllegalStateException("Raw data and pallete was null");
    }

    private void c(int n, char c2) {
        char[] cArray = this.a();
        if (cArray != null) {
            cArray[n] = c2;
        } else if (!this.b(n, c2)) {
            throw new IllegalStateException("Raw data and pallete was null");
        }
    }

    private synchronized boolean b(int n, char c2) {
        CharPalette charPalette = this.b;
        BitArray4096 bitArray4096 = this.d;
        if (charPalette != null && bitArray4096 != null) {
            char c3 = charPalette.getValue(c2);
            if (c3 != '\uffff') {
                bitArray4096.setAt(n, c3);
            } else {
                char[] cArray = bitArray4096.toRaw();
                for (int k = 0; k < 4096; ++k) {
                    cArray[k] = charPalette.getKey(cArray[k]);
                }
                cArray[n] = c2;
                this.c = cArray;
                this.d = null;
                this.b = null;
            }
            return true;
        }
        return false;
    }

    public synchronized boolean compress() {
        char[] cArray = this.c;
        if (cArray != null) {
            int n = 0;
            BitSet bitSet = ThreadCache.boolCache4096.get();
            char[] cArray2 = ThreadCache.charCache4096.get();
            char[] cArray3 = ThreadCache.charCache4096v2.get();
            bitSet.clear();
            for (char c2 : cArray) {
                if (bitSet.get(c2)) continue;
                cArray3[n] = c2;
                bitSet.set(c2);
                n = (char)(n + 1);
            }
            char[] cArray4 = Arrays.copyOfRange(cArray3, 0, n);
            if (cArray4.length > 1) {
                Arrays.sort(cArray4);
                for (int n2 = 0; n2 < cArray4.length; n2 = (int)((char)(n2 + 1))) {
                    cArray2[cArray4[n2]] = n2;
                }
            } else {
                cArray2[cArray4[0]] = '\u0000';
            }
            CharPalette charPalette = new CharPalette();
            charPalette.set(cArray4);
            int n3 = MathHelper.log2(n - 1);
            BitArray4096 bitArray4096 = new BitArray4096(n3);
            for (int k = 0; k < cArray.length; ++k) {
                cArray3[k] = cArray2[cArray[k]];
            }
            bitArray4096.fromRaw(cArray3);
            this.b = charPalette;
            this.d = bitArray4096;
            this.c = null;
            return true;
        }
        return false;
    }

    public synchronized BlockDataPalette clone() {
        return new BlockDataPalette((char[])this.getRaw().clone());
    }

    private static IllegalStateException a(IllegalStateException illegalStateException) {
        return illegalStateException;
    }
}

