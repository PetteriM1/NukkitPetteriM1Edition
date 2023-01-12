/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.util;

import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.util.BitArray;
import cn.nukkit.level.util.BitArrayVersion;
import cn.nukkit.utils.BinaryStream;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class PalettedBlockStorage {
    private static final int a = 4096;
    private final IntList c;
    private BitArray b;

    public static PalettedBlockStorage createFromBlockPalette() {
        return PalettedBlockStorage.createFromBlockPalette(BitArrayVersion.V2);
    }

    public static PalettedBlockStorage createFromBlockPalette(BitArrayVersion bitArrayVersion) {
        return PalettedBlockStorage.createFromBlockPalette(bitArrayVersion, 0);
    }

    public static PalettedBlockStorage createFromBlockPalette(int n) {
        return PalettedBlockStorage.createFromBlockPalette(BitArrayVersion.V2, n);
    }

    public static PalettedBlockStorage createFromBlockPalette(BitArrayVersion bitArrayVersion, int n) {
        int n2 = 0;
        if (n >= 419) {
            n2 = GlobalBlockPalette.getOrCreateRuntimeId(n, 0);
        }
        return new PalettedBlockStorage(bitArrayVersion, n2);
    }

    public static PalettedBlockStorage createWithDefaultState(int n) {
        return PalettedBlockStorage.createWithDefaultState(BitArrayVersion.V2, n);
    }

    public static PalettedBlockStorage createWithDefaultState(BitArrayVersion bitArrayVersion, int n) {
        return new PalettedBlockStorage(bitArrayVersion, n);
    }

    private PalettedBlockStorage(BitArrayVersion bitArrayVersion, int n) {
        this.b = bitArrayVersion.createPalette(4096);
        this.c = new IntArrayList(16);
        this.c.add(n);
    }

    private PalettedBlockStorage(BitArray bitArray, IntList intList) {
        this.c = intList;
        this.b = bitArray;
    }

    private int a(BitArrayVersion bitArrayVersion) {
        return bitArrayVersion.getId() << 1 | 1;
    }

    private int a(int n, int n2, int n3) {
        return n << 8 | n3 << 4 | n2;
    }

    public void setBlock(int n, int n2, int n3, int n4) {
        this.setBlock(this.a(n, n2, n3), n4);
    }

    public void setBlock(int n, int n2) {
        try {
            int n3 = this.a(n2);
            this.b.set(n, n3);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException("Unable to set block runtime ID: " + n2 + ", palette: " + this.c, illegalArgumentException);
        }
    }

    public void writeTo(BinaryStream binaryStream) {
        binaryStream.putByte((byte)this.a(this.b.getVersion()));
        for (int n : this.b.getWords()) {
            binaryStream.putLInt(n);
        }
        binaryStream.putVarInt(this.c.size());
        this.c.forEach(binaryStream::putVarInt);
    }

    private void b(BitArrayVersion bitArrayVersion) {
        BitArray bitArray = bitArrayVersion.createPalette();
        for (int k = 0; k < 4096; ++k) {
            bitArray.set(k, this.b.get(k));
        }
        this.b = bitArray;
    }

    private int a(int n) {
        BitArrayVersion bitArrayVersion;
        BitArrayVersion bitArrayVersion2;
        int n2 = this.c.indexOf(n);
        if (n2 != -1) {
            return n2;
        }
        n2 = this.c.size();
        if (n2 > (bitArrayVersion2 = this.b.getVersion()).getMaxEntryValue() && (bitArrayVersion = bitArrayVersion2.next()) != null) {
            this.b(bitArrayVersion);
        }
        this.c.add(n);
        return n2;
    }

    public boolean isEmpty() {
        if (this.c.size() == 1) {
            return true;
        }
        for (int n : this.b.getWords()) {
            if (Integer.toUnsignedLong(n) == 0L) continue;
            return false;
        }
        return true;
    }

    public PalettedBlockStorage copy() {
        return new PalettedBlockStorage(this.b.copy(), new IntArrayList(this.c));
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

