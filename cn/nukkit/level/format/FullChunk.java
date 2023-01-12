/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format;

import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.format.LevelProvider;
import java.io.IOException;
import java.util.Map;

public interface FullChunk
extends Cloneable {
    public int getX();

    public int getZ();

    default public void setPosition(int n, int n2) {
        this.setX(n);
        this.setZ(n2);
    }

    public void setX(int var1);

    public void setZ(int var1);

    public long getIndex();

    public LevelProvider getProvider();

    public void setProvider(LevelProvider var1);

    public int getFullBlock(int var1, int var2, int var3);

    public Block getAndSetBlock(int var1, int var2, int var3, Block var4);

    default public boolean setFullBlockId(int n, int n2, int n3, int n4) {
        return this.setBlock(n, n2, n3, n4 >> 4, n4 & 0xF);
    }

    public boolean setBlock(int var1, int var2, int var3, int var4);

    public boolean setBlock(int var1, int var2, int var3, int var4, int var5);

    public int getBlockId(int var1, int var2, int var3);

    public void setBlockId(int var1, int var2, int var3, int var4);

    public int getBlockData(int var1, int var2, int var3);

    public void setBlockData(int var1, int var2, int var3, int var4);

    public int getBlockExtraData(int var1, int var2, int var3);

    public void setBlockExtraData(int var1, int var2, int var3, int var4);

    public int getBlockSkyLight(int var1, int var2, int var3);

    public void setBlockSkyLight(int var1, int var2, int var3, int var4);

    public int getBlockLight(int var1, int var2, int var3);

    public void setBlockLight(int var1, int var2, int var3, int var4);

    public int getHighestBlockAt(int var1, int var2);

    public int getHighestBlockAt(int var1, int var2, boolean var3);

    public int getHeightMap(int var1, int var2);

    public void setHeightMap(int var1, int var2, int var3);

    public void recalculateHeightMap();

    public void populateSkyLight();

    public int getBiomeId(int var1, int var2);

    public void setBiomeIdAndColor(int var1, int var2, int var3);

    default public void setBiomeId(int n, int n2, int n3) {
        this.setBiomeId(n, n2, (byte)n3);
    }

    public void setBiomeId(int var1, int var2, byte var3);

    default public void setBiome(int n, int n2, Biome biome) {
        this.setBiomeId(n, n2, (byte)biome.getId());
    }

    public int getBiomeColor(int var1, int var2);

    public void setBiomeColor(int var1, int var2, int var3, int var4, int var5);

    public boolean isLightPopulated();

    public void setLightPopulated();

    public void setLightPopulated(boolean var1);

    public boolean isPopulated();

    public void setPopulated();

    public void setPopulated(boolean var1);

    public boolean isGenerated();

    public void setGenerated();

    public void setGenerated(boolean var1);

    public void addEntity(Entity var1);

    public void removeEntity(Entity var1);

    public void addBlockEntity(BlockEntity var1);

    public void removeBlockEntity(BlockEntity var1);

    public Map<Long, Entity> getEntities();

    public Map<Long, BlockEntity> getBlockEntities();

    public BlockEntity getTile(int var1, int var2, int var3);

    public boolean isLoaded();

    public boolean load() throws IOException;

    public boolean load(boolean var1) throws IOException;

    public boolean unload() throws Exception;

    public boolean unload(boolean var1) throws Exception;

    public boolean unload(boolean var1, boolean var2) throws Exception;

    public void initChunk();

    public byte[] getBiomeIdArray();

    public int[] getBiomeColorArray();

    public byte[] getHeightMapArray();

    public byte[] getBlockIdArray();

    public byte[] getBlockDataArray();

    public Map<Integer, Integer> getBlockExtraDataArray();

    public byte[] getBlockSkyLightArray();

    public byte[] getBlockLightArray();

    public byte[] toBinary();

    public byte[] toFastBinary();

    public boolean hasChanged();

    public void setChanged();

    public void setChanged(boolean var1);
}

