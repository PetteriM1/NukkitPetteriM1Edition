/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil.util;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.anvil.util.NibbleArray;
import cn.nukkit.level.util.PalettedBlockStorage;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import com.google.common.base.Preconditions;
import java.util.Arrays;

public class BlockStorage {
    private static final int a = 4096;
    private final byte[] c;
    private final byte[] d;
    private final NibbleArray b;

    public BlockStorage() {
        this.c = new byte[4096];
        this.d = new byte[4096];
        this.b = new NibbleArray(4096);
    }

    private BlockStorage(byte[] byArray, byte[] byArray2, NibbleArray nibbleArray) {
        this.c = byArray;
        this.d = byArray2;
        this.b = nibbleArray;
    }

    private static int a(int n, int n2, int n3) {
        int n4 = (n << 8) + (n3 << 4) + n2;
        if (n4 < 0 || n4 >= 4096) {
            throw new IllegalArgumentException("Invalid index");
        }
        return n4;
    }

    public int getBlockData(int n, int n2, int n3) {
        return this.b.get(BlockStorage.a(n, n2, n3)) & 0xF;
    }

    public int getBlockId(int n, int n2, int n3) {
        int n4 = BlockStorage.a(n, n2, n3);
        int n5 = (this.d[n4] & 0xFF) + 255;
        if (n5 == 255) {
            n5 = this.c[n4] & 0xFF;
        }
        return n5;
    }

    public int getBlockIdFor(int n, int n2, int n3, int n4) {
        int n5 = BlockStorage.a(n, n2, n3);
        if (n4 == 1) {
            return this.c[n5] & 0xFF;
        }
        if (n4 == 2) {
            return this.d[n5] & 0xFF;
        }
        throw new IllegalArgumentException("Unknown version");
    }

    public void setBlockId(int n, int n2, int n3, int n4) {
        block1: {
            int n5;
            block0: {
                n5 = BlockStorage.a(n, n2, n3);
                if (n4 <= 255) break block0;
                this.c[n5] = 0;
                this.d[n5] = (byte)(n4 - 255 & 0xFF);
                break block1;
            }
            int n6 = n4 & 0xFF;
            this.c[n5] = (byte)n6;
            this.d[n5] = 0;
            if (n6 != 0) break block1;
            this.b.remove(n5);
        }
    }

    public void setBlockData(int n, int n2, int n3, int n4) {
        this.b.set(BlockStorage.a(n, n2, n3), (byte)n4);
    }

    public int getFullBlock(int n, int n2, int n3) {
        return this.a(BlockStorage.a(n, n2, n3));
    }

    public void setFullBlock(int n, int n2, int n3, int n4) {
        this.a(BlockStorage.a(n, n2, n3), (short)n4);
    }

    public int getAndSetFullBlock(int n, int n2, int n3, int n4) {
        return this.b(BlockStorage.a(n, n2, n3), (short)n4);
    }

    private int b(int n, short s2) {
        byte by;
        int n2;
        block4: {
            Preconditions.checkArgument(s2 < 8191, "Invalid full block");
            n2 = (this.d[n] & 0xFF) + 255;
            if (n2 == 255) {
                n2 = this.c[n] & 0xFF;
            }
            by = this.b.get(n);
            int n3 = (s2 & 0x1FF0) >> 4;
            byte by2 = (byte)(s2 & 0xF);
            if (n2 != n3) {
                if (n3 > 255) {
                    this.c[n] = 0;
                    this.d[n] = (byte)(n3 - 255 & 0xFF);
                } else {
                    this.c[n] = (byte)(n3 & 0xFF);
                    this.d[n] = 0;
                }
            }
            if (by == by2) break block4;
            this.b.set(n, by2);
        }
        return n2 << 4 | by;
    }

    private int a(int n) {
        int n2 = (this.d[n] & 0xFF) + 255;
        if (n2 == 255) {
            n2 = this.c[n] & 0xFF;
        }
        return n2 << 4 | this.b.get(n);
    }

    private void a(int n, short s2) {
        Preconditions.checkArgument(s2 < 8191, "Invalid full block");
        int n2 = (s2 & 0x1FF0) >> 4;
        byte by = (byte)(s2 & 0xF);
        if (n2 > 255) {
            this.c[n] = 0;
            this.d[n] = (byte)(n2 - 255 & 0xFF);
        } else {
            this.c[n] = (byte)(n2 & 0xFF);
            this.d[n] = 0;
        }
        this.b.set(n, by);
    }

    public byte[] getBlockIds() {
        return this.getBlockIds(1);
    }

    public byte[] getBlockIds(int n) {
        if (n == 1) {
            return Arrays.copyOf(this.c, this.c.length);
        }
        if (n == 2) {
            return Arrays.copyOf(this.d, this.d.length);
        }
        throw new IllegalArgumentException("Unknown version");
    }

    public byte[] getBlockData() {
        return this.getBlockData(false);
    }

    public byte[] getBlockData(boolean bl) {
        if (bl) {
            return Arrays.copyOf(this.b.getData(), this.b.getData().length);
        }
        return this.b.getData();
    }

    public void writeTo(BinaryStream binaryStream) {
        Server.mvw("BlockStorage#writeTo(BinaryStream)");
        this.writeTo(ProtocolInfo.CURRENT_PROTOCOL, binaryStream, false);
    }

    public void writeTo(int n, BinaryStream binaryStream, boolean bl) {
        PalettedBlockStorage palettedBlockStorage = PalettedBlockStorage.createFromBlockPalette(n);
        PalettedBlockStorage palettedBlockStorage2 = PalettedBlockStorage.createFromBlockPalette(n);
        int n2 = GlobalBlockPalette.getOrCreateRuntimeId(n, 8, 0);
        for (int k = 0; k < 4096; ++k) {
            int n3 = (this.d[k] & 0xFF) + 255;
            if (n3 == 255) {
                n3 = this.c[k] & 0xFF;
            }
            byte by = this.b.get(k);
            if (bl && Level.xrayableBlocks[n3]) {
                n3 = 1;
            }
            palettedBlockStorage.setBlock(k, GlobalBlockPalette.getOrCreateRuntimeId(n, n3, by));
            if (!Block.usesFakeWater(n3)) continue;
            palettedBlockStorage2.setBlock(k, n2);
        }
        palettedBlockStorage.writeTo(binaryStream);
        palettedBlockStorage2.writeTo(binaryStream);
    }

    public BlockStorage copy() {
        return new BlockStorage((byte[])this.c.clone(), (byte[])this.d.clone(), this.b.copy());
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

