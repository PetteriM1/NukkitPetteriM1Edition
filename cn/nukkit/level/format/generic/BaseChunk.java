/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.generic;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.level.format.Chunk;
import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.format.generic.EmptyChunkSection;
import cn.nukkit.utils.ChunkException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class BaseChunk
extends BaseFullChunk
implements Chunk {
    public ChunkSection[] sections;
    private static final byte[] d = new byte[4096];
    private static final byte[] e = new byte[2048];

    @Override
    public BaseChunk clone() {
        BaseChunk baseChunk = (BaseChunk)super.clone();
        if (this.biomes != null) {
            baseChunk.biomes = (byte[])this.biomes.clone();
        }
        baseChunk.heightMap = (byte[])this.getHeightMapArray().clone();
        if (this.sections != null && this.sections[0] != null) {
            baseChunk.sections = new ChunkSection[this.sections.length];
            for (int k = 0; k < this.sections.length; ++k) {
                baseChunk.sections[k] = this.sections[k].copy();
            }
        }
        return baseChunk;
    }

    @Override
    protected BaseChunk fullClone() {
        BaseChunk baseChunk = (BaseChunk)super.fullClone();
        if (this.biomes != null) {
            baseChunk.biomes = (byte[])this.biomes.clone();
        }
        baseChunk.heightMap = (byte[])this.getHeightMapArray().clone();
        if (this.sections != null && this.sections[0] != null) {
            baseChunk.sections = new ChunkSection[this.sections.length];
            for (int k = 0; k < this.sections.length; ++k) {
                baseChunk.sections[k] = this.sections[k].copy();
            }
        }
        return baseChunk;
    }

    private void a(int n, int n2, int n3) {
        block2: {
            BlockEntity blockEntity = this.getTile(n, n2, n3);
            if (blockEntity == null || blockEntity.isBlockEntityValid()) break block2;
            this.removeBlockEntity(blockEntity);
            if (!blockEntity.closed) {
                blockEntity.closed = true;
                if (blockEntity.level != null) {
                    blockEntity.level.removeBlockEntity(blockEntity);
                    blockEntity.level = null;
                }
            }
        }
    }

    @Override
    public int getFullBlock(int n, int n2, int n3) {
        return this.sections[n2 >> 4].getFullBlock(n, n2 & 0xF, n3);
    }

    @Override
    public boolean setBlock(int n, int n2, int n3, int n4) {
        return this.setBlock(n, n2, n3, n4, 0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Block getAndSetBlock(int n, int n2, int n3, Block block) {
        int n4 = n2 >> 4;
        try {
            this.setChanged();
            Block block2 = this.sections[n4].getAndSetBlock(n, n2 & 0xF, n3, block);
            return block2;
        }
        catch (ChunkException chunkException) {
            try {
                this.a(n4, (ChunkSection)this.providerClass.getMethod("createChunkSection", Integer.TYPE).invoke(this.providerClass, n4));
            }
            catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
                Server.getInstance().getLogger().logException(reflectiveOperationException);
            }
            Block block3 = this.sections[n4].getAndSetBlock(n, n2 & 0xF, n3, block);
            return block3;
        }
        finally {
            this.a(n, n2, n3);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean setFullBlockId(int n, int n2, int n3, int n4) {
        int n5 = n2 >> 4;
        try {
            this.setChanged();
            boolean bl = this.sections[n5].setFullBlockId(n, n2 & 0xF, n3, n4);
            return bl;
        }
        catch (ChunkException chunkException) {
            try {
                this.a(n5, (ChunkSection)this.providerClass.getMethod("createChunkSection", Integer.TYPE).invoke(this.providerClass, n5));
            }
            catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
                Server.getInstance().getLogger().logException(reflectiveOperationException);
            }
            boolean bl = this.sections[n5].setFullBlockId(n, n2 & 0xF, n3, n4);
            return bl;
        }
        finally {
            this.a(n, n2, n3);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean setBlock(int n, int n2, int n3, int n4, int n5) {
        int n6 = n2 >> 4;
        try {
            this.setChanged();
            boolean bl = this.sections[n6].setBlock(n, n2 & 0xF, n3, n4, n5);
            return bl;
        }
        catch (ChunkException chunkException) {
            try {
                this.a(n6, (ChunkSection)this.providerClass.getMethod("createChunkSection", Integer.TYPE).invoke(this.providerClass, n6));
            }
            catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
                Server.getInstance().getLogger().logException(reflectiveOperationException);
            }
            boolean bl = this.sections[n6].setBlock(n, n2 & 0xF, n3, n4, n5);
            return bl;
        }
        finally {
            this.a(n, n2, n3);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setBlockId(int n, int n2, int n3, int n4) {
        int n5 = n2 >> 4;
        try {
            this.sections[n5].setBlockId(n, n2 & 0xF, n3, n4);
            this.setChanged();
        }
        catch (ChunkException chunkException) {
            try {
                this.a(n5, (ChunkSection)this.providerClass.getMethod("createChunkSection", Integer.TYPE).invoke(this.providerClass, n5));
            }
            catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
                Server.getInstance().getLogger().logException(reflectiveOperationException);
            }
            this.sections[n5].setBlockId(n, n2 & 0xF, n3, n4);
        }
        finally {
            this.a(n, n2, n3);
        }
    }

    @Override
    public int getBlockId(int n, int n2, int n3) {
        return this.sections[n2 >> 4].getBlockId(n, n2 & 0xF, n3);
    }

    @Override
    public int getBlockData(int n, int n2, int n3) {
        return this.sections[n2 >> 4].getBlockData(n, n2 & 0xF, n3);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setBlockData(int n, int n2, int n3, int n4) {
        int n5 = n2 >> 4;
        try {
            this.sections[n5].setBlockData(n, n2 & 0xF, n3, n4);
            this.setChanged();
        }
        catch (ChunkException chunkException) {
            try {
                this.a(n5, (ChunkSection)this.providerClass.getMethod("createChunkSection", Integer.TYPE).invoke(this.providerClass, n5));
            }
            catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
                Server.getInstance().getLogger().logException(reflectiveOperationException);
            }
            this.sections[n5].setBlockData(n, n2 & 0xF, n3, n4);
        }
        finally {
            this.a(n, n2, n3);
        }
    }

    @Override
    public int getBlockSkyLight(int n, int n2, int n3) {
        return this.sections[n2 >> 4].getBlockSkyLight(n, n2 & 0xF, n3);
    }

    @Override
    public void setBlockSkyLight(int n, int n2, int n3, int n4) {
        int n5 = n2 >> 4;
        try {
            this.sections[n5].setBlockSkyLight(n, n2 & 0xF, n3, n4);
            this.setChanged();
        }
        catch (ChunkException chunkException) {
            try {
                this.a(n5, (ChunkSection)this.providerClass.getMethod("createChunkSection", Integer.TYPE).invoke(this.providerClass, n5));
            }
            catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
                Server.getInstance().getLogger().logException(reflectiveOperationException);
            }
            this.sections[n5].setBlockSkyLight(n, n2 & 0xF, n3, n4);
        }
    }

    @Override
    public int getBlockLight(int n, int n2, int n3) {
        return this.sections[n2 >> 4].getBlockLight(n, n2 & 0xF, n3);
    }

    @Override
    public void setBlockLight(int n, int n2, int n3, int n4) {
        int n5 = n2 >> 4;
        try {
            this.sections[n5].setBlockLight(n, n2 & 0xF, n3, n4);
            this.setChanged();
        }
        catch (ChunkException chunkException) {
            try {
                this.a(n5, (ChunkSection)this.providerClass.getMethod("createChunkSection", Integer.TYPE).invoke(this.providerClass, n5));
            }
            catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
                Server.getInstance().getLogger().logException(reflectiveOperationException);
            }
            this.sections[n5].setBlockLight(n, n2 & 0xF, n3, n4);
        }
    }

    @Override
    public boolean isSectionEmpty(float f2) {
        return this.sections[(int)f2] instanceof EmptyChunkSection;
    }

    @Override
    public ChunkSection getSection(float f2) {
        return this.sections[(int)f2];
    }

    @Override
    public boolean setSection(float f2, ChunkSection chunkSection) {
        this.sections[(int)f2] = Arrays.equals(d, chunkSection.getIdArray()) && Arrays.equals(e, chunkSection.getDataArray()) ? EmptyChunkSection.EMPTY[(int)f2] : chunkSection;
        this.setChanged();
        return true;
    }

    private void a(float f2, ChunkSection chunkSection) {
        this.sections[(int)f2] = chunkSection;
        this.setChanged();
    }

    @Override
    public boolean load() throws IOException {
        return this.load(true);
    }

    @Override
    public boolean load(boolean bl) throws IOException {
        return this.provider != null && this.provider.getChunk(this.getX(), this.getZ(), true) != null;
    }

    @Override
    public byte[] getBlockIdArray() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(65536);
        for (int k = 0; k < 16; ++k) {
            byteBuffer.put(this.sections[k].getIdArray());
        }
        return byteBuffer.array();
    }

    @Override
    public byte[] getBlockDataArray() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(32768);
        for (int k = 0; k < 16; ++k) {
            byteBuffer.put(this.sections[k].getDataArray());
        }
        return byteBuffer.array();
    }

    @Override
    public byte[] getBlockSkyLightArray() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(32768);
        for (int k = 0; k < 16; ++k) {
            byteBuffer.put(this.sections[k].getSkyLightArray());
        }
        return byteBuffer.array();
    }

    @Override
    public byte[] getBlockLightArray() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(32768);
        for (int k = 0; k < 16; ++k) {
            byteBuffer.put(this.sections[k].getLightArray());
        }
        return byteBuffer.array();
    }

    @Override
    public ChunkSection[] getSections() {
        return this.sections;
    }

    @Override
    public int[] getBiomeColorArray() {
        return new int[0];
    }

    @Override
    public byte[] getHeightMapArray() {
        return this.heightMap;
    }

    @Override
    public LevelProvider getProvider() {
        return this.provider;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

