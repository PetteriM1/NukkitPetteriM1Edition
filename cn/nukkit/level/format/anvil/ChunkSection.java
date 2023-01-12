/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.format.anvil.util.BlockStorage;
import cn.nukkit.level.format.anvil.util.NibbleArray;
import cn.nukkit.level.format.generic.EmptyChunkSection;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Utils;
import cn.nukkit.utils.Zlib;
import java.io.IOException;
import java.util.Arrays;

public class ChunkSection
implements cn.nukkit.level.format.ChunkSection {
    private final int b;
    private final BlockStorage a;
    protected byte[] blockLight;
    protected byte[] skyLight;
    protected byte[] compressedLight;
    protected boolean hasBlockLight;
    protected boolean hasSkyLight;

    private ChunkSection(int n, BlockStorage blockStorage, byte[] byArray, byte[] byArray2, byte[] byArray3, boolean bl, boolean bl2) {
        this.b = n;
        this.a = blockStorage;
        this.blockLight = byArray;
        this.skyLight = byArray2;
        this.compressedLight = byArray3;
        this.hasBlockLight = bl;
        this.hasSkyLight = bl2;
    }

    public ChunkSection(int n) {
        this.b = n;
        this.hasBlockLight = false;
        this.hasSkyLight = false;
        this.a = new BlockStorage();
    }

    public ChunkSection(CompoundTag compoundTag) {
        this.b = compoundTag.getByte("Y");
        byte[] byArray = compoundTag.getByteArray("Blocks");
        byte[] byArray2 = compoundTag.getByteArray("Blocks2PM1E", 4096);
        NibbleArray nibbleArray = new NibbleArray(compoundTag.getByteArray("Data"));
        this.a = new BlockStorage();
        for (int k = 0; k < 16; ++k) {
            for (int i2 = 0; i2 < 16; ++i2) {
                for (int i3 = 0; i3 < 16; ++i3) {
                    int n = ChunkSection.a(k, i3, i2);
                    this.a.setBlockData(k, i3, i2, nibbleArray.get(n));
                    int n2 = (byArray2[n] & 0xFF) + 255;
                    if (n2 == 255) {
                        n2 = byArray[n] & 0xFF;
                    }
                    this.a.setBlockId(k, i3, i2, n2);
                }
            }
        }
        this.blockLight = compoundTag.getByteArray("BlockLight");
        this.skyLight = compoundTag.getByteArray("SkyLight");
    }

    private static int a(int n, int n2, int n3) {
        return (n2 << 8) + (n3 << 4) + n;
    }

    @Override
    public int getY() {
        return this.b;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int getBlockId(int n, int n2, int n3) {
        BlockStorage blockStorage = this.a;
        synchronized (blockStorage) {
            return this.a.getBlockId(n, n2, n3);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setBlockId(int n, int n2, int n3, int n4) {
        BlockStorage blockStorage = this.a;
        synchronized (blockStorage) {
            this.a.setBlockId(n, n2, n3, n4);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean setFullBlockId(int n, int n2, int n3, int n4) {
        BlockStorage blockStorage = this.a;
        synchronized (blockStorage) {
            this.a.setFullBlock(n, n2, n3, (char)n4);
        }
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int getBlockData(int n, int n2, int n3) {
        BlockStorage blockStorage = this.a;
        synchronized (blockStorage) {
            return this.a.getBlockData(n, n2, n3);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setBlockData(int n, int n2, int n3, int n4) {
        BlockStorage blockStorage = this.a;
        synchronized (blockStorage) {
            this.a.setBlockData(n, n2, n3, n4);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int getFullBlock(int n, int n2, int n3) {
        BlockStorage blockStorage = this.a;
        synchronized (blockStorage) {
            return this.a.getFullBlock(n, n2, n3);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean setBlock(int n, int n2, int n3, int n4) {
        BlockStorage blockStorage = this.a;
        synchronized (blockStorage) {
            return this.setBlock(n, n2, n3, n4, 0);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Block getAndSetBlock(int n, int n2, int n3, Block block) {
        BlockStorage blockStorage = this.a;
        synchronized (blockStorage) {
            int n4 = this.a.getAndSetFullBlock(n, n2, n3, block.getFullId());
            return Block.fullList[n4].clone();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean setBlock(int n, int n2, int n3, int n4, int n5) {
        int n6 = (n4 << 4) + n5;
        BlockStorage blockStorage = this.a;
        synchronized (blockStorage) {
            int n7 = this.a.getAndSetFullBlock(n, n2, n3, n6);
            return n6 != n7;
        }
    }

    @Override
    public int getBlockSkyLight(int n, int n2, int n3) {
        if (this.skyLight == null) {
            if (!this.hasSkyLight) {
                return 0;
            }
            if (this.compressedLight == null) {
                return 15;
            }
        }
        this.skyLight = this.getSkyLightArray();
        int n4 = this.skyLight[n2 << 7 | n3 << 3 | n >> 1] & 0xFF;
        if ((n & 1) == 0) {
            return n4 & 0xF;
        }
        return n4 >> 4;
    }

    @Override
    public void setBlockSkyLight(int n, int n2, int n3, int n4) {
        if (this.skyLight == null) {
            if (this.hasSkyLight && this.compressedLight != null) {
                this.skyLight = this.getSkyLightArray();
            } else {
                if (n4 == (this.hasSkyLight ? 15 : 0)) {
                    return;
                }
                this.skyLight = new byte[2048];
                if (this.hasSkyLight) {
                    Arrays.fill(this.skyLight, (byte)-1);
                }
            }
        }
        int n5 = n2 << 7 | n3 << 3 | n >> 1;
        int n6 = this.skyLight[n5] & 0xFF;
        this.skyLight[n5] = (n & 1) == 0 ? (byte)(n6 & 0xF0 | n4 & 0xF) : (byte)((n4 & 0xF) << 4 | n6 & 0xF);
    }

    @Override
    public int getBlockLight(int n, int n2, int n3) {
        if (this.blockLight == null && !this.hasBlockLight) {
            return 0;
        }
        this.blockLight = this.getLightArray();
        int n4 = this.blockLight[n2 << 7 | n3 << 3 | n >> 1] & 0xFF;
        if ((n & 1) == 0) {
            return n4 & 0xF;
        }
        return n4 >> 4;
    }

    @Override
    public void setBlockLight(int n, int n2, int n3, int n4) {
        if (this.blockLight == null) {
            if (this.hasBlockLight) {
                this.blockLight = this.getLightArray();
            } else {
                if (n4 == 0) {
                    return;
                }
                this.blockLight = new byte[2048];
            }
        }
        int n5 = n2 << 7 | n3 << 3 | n >> 1;
        int n6 = this.blockLight[n5] & 0xFF;
        this.blockLight[n5] = (n & 1) == 0 ? (byte)(n6 & 0xF0 | n4 & 0xF) : (byte)((n4 & 0xF) << 4 | n6 & 0xF);
    }

    @Override
    public byte[] getIdArray() {
        return this.getIdArray(1);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte[] getIdArray(int n) {
        BlockStorage blockStorage = this.a;
        synchronized (blockStorage) {
            byte[] byArray = new byte[4096];
            for (int k = 0; k < 16; ++k) {
                for (int i2 = 0; i2 < 16; ++i2) {
                    for (int i3 = 0; i3 < 16; ++i3) {
                        int n2 = ChunkSection.a(k, i3, i2);
                        byArray[n2] = (byte)this.a.getBlockIdFor(k, i3, i2, n);
                    }
                }
            }
            return byArray;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte[] getDataArray() {
        BlockStorage blockStorage = this.a;
        synchronized (blockStorage) {
            NibbleArray nibbleArray = new NibbleArray(4096);
            for (int k = 0; k < 16; ++k) {
                for (int i2 = 0; i2 < 16; ++i2) {
                    for (int i3 = 0; i3 < 16; ++i3) {
                        int n = ChunkSection.a(k, i3, i2);
                        nibbleArray.set(n, (byte)this.a.getBlockData(k, i3, i2));
                    }
                }
            }
            return nibbleArray.getData();
        }
    }

    @Override
    public byte[] getSkyLightArray() {
        if (this.skyLight != null) {
            return this.skyLight;
        }
        if (this.hasSkyLight && this.compressedLight != null) {
            this.a();
            if (this.skyLight != null) {
                return this.skyLight;
            }
        }
        return EmptyChunkSection.EMPTY_SKY_LIGHT_ARR;
    }

    private void a() {
        try {
            if (this.compressedLight != null && this.compressedLight.length != 0) {
                byte[] byArray = Zlib.inflate(this.compressedLight);
                this.blockLight = Arrays.copyOfRange(byArray, 0, 2048);
                if (byArray.length > 2048) {
                    this.skyLight = Arrays.copyOfRange(byArray, 2048, 4096);
                } else {
                    this.skyLight = new byte[2048];
                    if (this.hasSkyLight) {
                        Arrays.fill(this.skyLight, (byte)-1);
                    }
                }
                this.compressedLight = null;
            } else {
                this.blockLight = new byte[2048];
                this.skyLight = new byte[2048];
                if (this.hasSkyLight) {
                    Arrays.fill(this.skyLight, (byte)-1);
                }
            }
        }
        catch (IOException iOException) {
            Server.getInstance().getLogger().logException(iOException);
        }
    }

    @Override
    public byte[] getLightArray() {
        if (this.blockLight != null) {
            return this.blockLight;
        }
        if (this.hasBlockLight) {
            this.a();
            if (this.blockLight != null) {
                return this.blockLight;
            }
        }
        return EmptyChunkSection.EMPTY_LIGHT_ARR;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte[] getBytes(boolean bl) {
        byte[] byArray;
        byte[] byArray2;
        byte[] byArray3;
        byte[] byArray4 = new byte[4096];
        BlockStorage blockStorage = this.a;
        synchronized (blockStorage) {
            byArray3 = this.a.getBlockIds(1);
            byArray2 = this.a.getBlockIds(2);
            byArray = this.a.getBlockData(true);
        }
        for (int k = 0; k < 4096; ++k) {
            int n = byArray2[k] + 255;
            n = n == 255 ? byArray3[k] : 248;
            byArray4[k] = (byte)n;
        }
        byte[] byArray5 = new byte[4096 + byArray.length];
        System.arraycopy(byArray4, 0, byArray5, 0, 4096);
        System.arraycopy(byArray, 0, byArray5, 4096, byArray.length);
        return byArray5;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void writeTo(int n, BinaryStream binaryStream, boolean bl) {
        BlockStorage blockStorage = this.a;
        synchronized (blockStorage) {
            binaryStream.putByte((byte)8);
            binaryStream.putByte((byte)2);
            this.a.writeTo(n, binaryStream, bl);
        }
    }

    public boolean compress() {
        if (this.blockLight != null) {
            byte[] byArray;
            byte[] byArray2 = this.blockLight;
            boolean bl = this.hasBlockLight = !Utils.isByteArrayEmpty(byArray2);
            if (this.skyLight != null) {
                byArray = this.skyLight;
                this.hasSkyLight = !Utils.isByteArrayEmpty(byArray);
            } else if (this.hasSkyLight) {
                byArray = EmptyChunkSection.EMPTY_SKY_LIGHT_ARR;
            } else {
                byArray = EmptyChunkSection.EMPTY_LIGHT_ARR;
                this.hasSkyLight = false;
            }
            this.blockLight = null;
            this.skyLight = null;
            byte[] byArray3 = null;
            if (this.hasBlockLight && this.hasSkyLight && byArray != EmptyChunkSection.EMPTY_SKY_LIGHT_ARR) {
                byArray3 = Binary.appendBytes(byArray2, (byte[][])new byte[][]{byArray});
            } else if (this.hasBlockLight) {
                byArray3 = byArray2;
            }
            if (byArray3 != null) {
                try {
                    this.compressedLight = Zlib.deflate(byArray3, 1);
                }
                catch (Exception exception) {
                    Server.getInstance().getLogger().logException(exception);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public ChunkSection copy() {
        return new ChunkSection(this.b, this.a.copy(), this.blockLight == null ? null : (byte[])this.blockLight.clone(), this.skyLight == null ? null : (byte[])this.skyLight.clone(), this.compressedLight == null ? null : (byte[])this.compressedLight.clone(), this.hasBlockLight, this.hasSkyLight);
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

