/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil.palette;

import cn.nukkit.level.format.anvil.palette.BitArray;
import cn.nukkit.level.format.anvil.palette.CharPalette;
import cn.nukkit.level.format.anvil.palette.a;
import cn.nukkit.level.format.anvil.palette.b;
import cn.nukkit.level.format.anvil.palette.c;
import cn.nukkit.math.MathHelper;
import java.util.Arrays;

public final class DataPalette
implements Cloneable {
    protected static final ThreadLocal<boolean[]> countCache = new c();
    protected static final ThreadLocal<char[]> mapFullToBitCache = new a();
    protected static final ThreadLocal<char[]> mapBitToFullCache = new b();
    private char[] b;
    private BitArray c;
    private CharPalette a;

    public DataPalette() {
        this(new char[4096]);
    }

    public DataPalette(char[] cArray) {
        this.b = cArray;
    }

    public synchronized char[] getRaw() {
        char[] cArray = this.b;
        if (cArray == null) {
            cArray = this.c.toRaw();
            for (int k = 0; k < 4096; ++k) {
                cArray[k] = this.a.getKey(cArray[k]);
            }
        }
        this.b = cArray;
        this.c = null;
        this.a = null;
        return this.b;
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

    public void setBlockData(int n, int n2, int n3, int n4) {
        int n5 = this.a(n, n2, n3);
        char[] cArray = this.b;
        if (cArray != null) {
            char c2 = cArray[n5];
            cArray[n5] = (char)(c2 & 0xFFF0 | n4);
        } else {
            char c3 = this.a.getKey(this.c.getAt(n5));
            if ((c3 & 0xF) != n4) {
                this.a(n5, (char)(c3 & 0xFFF0 | n4));
            }
        }
    }

    public int getFullBlock(int n, int n2, int n3) {
        return this.a(this.a(n, n2, n3));
    }

    public void setFullBlock(int n, int n2, int n3, int n4) {
        this.c(this.a(n, n2, n3), (char)n4);
    }

    public int getAndSetFullBlock(int n, int n2, int n3, int n4) {
        return this.b(this.a(n, n2, n3), (char)n4);
    }

    private int b(int n, char c2) {
        char[] cArray = this.b;
        if (cArray != null) {
            char c3 = cArray[n];
            cArray[n] = c2;
            return c3;
        }
        char c4 = this.a.getKey(this.c.getAt(n));
        if (c4 != c2) {
            this.a(n, c2);
        }
        return c4;
    }

    private int a(int n) {
        char[] cArray = this.b;
        if (cArray != null) {
            return cArray[n];
        }
        return this.a.getKey(this.c.getAt(n));
    }

    private void c(int n, char c2) {
        char[] cArray = this.b;
        if (cArray != null) {
            cArray[n] = c2;
            return;
        }
        this.a(n, c2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void a(int n, char c2) {
        char c3 = this.a.getValue(c2);
        if (c3 != '\uffff') {
            this.c.setAt(n, c3);
        } else {
            DataPalette dataPalette = this;
            synchronized (dataPalette) {
                char[] cArray = this.c.toRaw();
                for (int k = 0; k < 4096; ++k) {
                    cArray[k] = this.a.getKey(cArray[k]);
                }
                cArray[n] = c2;
                this.b = cArray;
                this.c = null;
                this.a = null;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean compress() {
        char[] cArray = this.b;
        if (cArray != null) {
            DataPalette dataPalette = this;
            synchronized (dataPalette) {
                int n = 0;
                boolean[] blArray = countCache.get();
                char[] cArray2 = mapFullToBitCache.get();
                char[] cArray3 = mapBitToFullCache.get();
                Arrays.fill(blArray, false);
                for (char c2 : cArray) {
                    if (blArray[c2]) continue;
                    cArray3[n] = c2;
                    blArray[c2] = true;
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
                int n3 = MathHelper.log2nlz(n) + 1;
                BitArray bitArray = new BitArray(n3);
                for (int k = 0; k < cArray.length; ++k) {
                    cArray[k] = cArray2[cArray[k]];
                }
                bitArray.fromRaw(cArray);
                this.a = charPalette;
                this.c = bitArray;
                this.b = null;
                return true;
            }
        }
        return false;
    }

    public synchronized DataPalette clone() {
        return new DataPalette((char[])this.getRaw().clone());
    }
}

