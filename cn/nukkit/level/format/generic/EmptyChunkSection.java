/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.generic;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.util.BitArrayVersion;
import cn.nukkit.level.util.PalettedBlockStorage;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.ChunkException;
import java.util.Arrays;

public class EmptyChunkSection
implements ChunkSection {
    public static final EmptyChunkSection[] EMPTY = new EmptyChunkSection[16];
    private static final PalettedBlockStorage b = PalettedBlockStorage.createFromBlockPalette(BitArrayVersion.V1, 0);
    private static final PalettedBlockStorage a = PalettedBlockStorage.createFromBlockPalette(BitArrayVersion.V1, 419);
    public static byte[] EMPTY_LIGHT_ARR;
    public static byte[] EMPTY_SKY_LIGHT_ARR;
    private final int c;

    public EmptyChunkSection(int n) {
        this.c = n;
    }

    @Override
    public int getY() {
        return this.c;
    }

    @Override
    public final int getBlockId(int n, int n2, int n3) {
        return 0;
    }

    @Override
    public int getFullBlock(int n, int n2, int n3) throws ChunkException {
        return 0;
    }

    @Override
    public Block getAndSetBlock(int n, int n2, int n3, Block block) {
        if (block.getId() != 0) {
            throw new ChunkException("Tried to modify an empty Chunk");
        }
        return Block.get(0);
    }

    @Override
    public boolean setBlock(int n, int n2, int n3, int n4) throws ChunkException {
        if (n4 != 0) {
            throw new ChunkException("Tried to modify an empty Chunk");
        }
        return false;
    }

    @Override
    public boolean setBlock(int n, int n2, int n3, int n4, int n5) throws ChunkException {
        if (n4 != 0) {
            throw new ChunkException("Tried to modify an empty Chunk");
        }
        return false;
    }

    @Override
    public byte[] getIdArray() {
        return this.getIdArray(1);
    }

    @Override
    public byte[] getIdArray(int n) {
        return new byte[4096];
    }

    @Override
    public byte[] getDataArray() {
        return new byte[2048];
    }

    @Override
    public byte[] getSkyLightArray() {
        return EMPTY_SKY_LIGHT_ARR;
    }

    @Override
    public byte[] getLightArray() {
        return EMPTY_LIGHT_ARR;
    }

    @Override
    public final void setBlockId(int n, int n2, int n3, int n4) throws ChunkException {
        if (n4 != 0) {
            throw new ChunkException("Tried to modify an empty Chunk");
        }
    }

    @Override
    public final int getBlockData(int n, int n2, int n3) {
        return 0;
    }

    @Override
    public void setBlockData(int n, int n2, int n3, int n4) throws ChunkException {
        if (n4 != 0) {
            throw new ChunkException("Tried to modify an empty Chunk");
        }
    }

    @Override
    public boolean setFullBlockId(int n, int n2, int n3, int n4) {
        if (n4 != 0) {
            throw new ChunkException("Tried to modify an empty Chunk");
        }
        return false;
    }

    @Override
    public int getBlockLight(int n, int n2, int n3) {
        return 0;
    }

    @Override
    public void setBlockLight(int n, int n2, int n3, int n4) throws ChunkException {
        if (n4 != 0) {
            throw new ChunkException("Tried to modify an empty Chunk");
        }
    }

    @Override
    public int getBlockSkyLight(int n, int n2, int n3) {
        return 15;
    }

    @Override
    public void setBlockSkyLight(int n, int n2, int n3, int n4) throws ChunkException {
        if (n4 != 15) {
            throw new ChunkException("Tried to modify an empty Chunk");
        }
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public byte[] getBytes(boolean bl) {
        return new byte[6144];
    }

    @Override
    public void writeTo(int n, BinaryStream binaryStream, boolean bl) {
        binaryStream.putByte((byte)8);
        if (n >= 503) {
            binaryStream.putByte((byte)0);
        } else {
            binaryStream.putByte((byte)2);
            if (n >= 419) {
                a.writeTo(binaryStream);
                a.writeTo(binaryStream);
            } else {
                b.writeTo(binaryStream);
                b.writeTo(binaryStream);
            }
        }
    }

    @Override
    public EmptyChunkSection copy() {
        return this;
    }

    static {
        for (int k = 0; k < EMPTY.length; ++k) {
            EmptyChunkSection.EMPTY[k] = new EmptyChunkSection(k);
        }
        EMPTY_LIGHT_ARR = new byte[2048];
        EMPTY_SKY_LIGHT_ARR = new byte[2048];
        Arrays.fill(EMPTY_SKY_LIGHT_ARR, (byte)-1);
    }

    private static ChunkException a(ChunkException chunkException) {
        return chunkException;
    }
}

