package cn.nukkit.level.format;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLayer;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.util.PalettedBlockStorage;

import java.io.IOException;
import java.util.Map;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public interface FullChunk extends Cloneable {

    void addBlockEntity(BlockEntity blockEntity);

    void addEntity(Entity entity);

    default Block getAndSetBlock(int x, int y, int z, Block block) {
        return this.getAndSetBlock(x, y, z, Block.LAYER_NORMAL, block);
    }

    Block getAndSetBlock(int x, int y, int z, BlockLayer layer, Block block);

    @Deprecated
    default int getBiomeColor(int x, int z) {
        return 0;
    }

    @Deprecated
    default int[] getBiomeColorArray() {
        return new int[0];
    }

    int getBiomeId(int x, int z);

    default int getBiomeId(int x, int y, int z) {
        return this.getBiomeId(x, z);
    }

    byte[] getBiomeIdArray();

    default PalettedBlockStorage getBiomeStorage(int y) {
        return null;
    }

    default int getBlockData(int x, int y, int z) {
        return this.getBlockData(x, y, z, Block.LAYER_NORMAL);
    }

    int getBlockData(int x, int y, int z, BlockLayer layer);

    @Deprecated
    byte[] getBlockDataArray();

    Map<Long, BlockEntity> getBlockEntities();

    int getBlockExtraData(int x, int y, int z);

    Map<Integer, Integer> getBlockExtraDataArray();

    default int getBlockId(int x, int y, int z) {
        return this.getBlockId(x, y, z, Block.LAYER_NORMAL);
    }

    int getBlockId(int x, int y, int z, BlockLayer layer);

    @Deprecated
    byte[] getBlockIdArray();

    int getBlockLight(int x, int y, int z);

    @Deprecated
    byte[] getBlockLightArray();

    int getBlockSkyLight(int x, int y, int z);

    @Deprecated
    byte[] getBlockSkyLightArray();

    Map<Long, Entity> getEntities();

    default int getFullBlock(int x, int y, int z) {
        return this.getFullBlock(x, y, z, Block.LAYER_NORMAL);
    }

    int getFullBlock(int x, int y, int z, BlockLayer layer);

    int getHeightMap(int x, int z);

    byte[] getHeightMapArray();

    int getHighestBlockAt(int x, int z);

    int getHighestBlockAt(int x, int z, boolean cache);

    long getIndex();

    LevelProvider getProvider();

    BlockEntity getTile(int x, int y, int z);

    int getX();

    int getZ();

    default boolean has3dBiomes() {
        return false;
    }

    boolean hasChanged();

    void initChunk();

    boolean isGenerated();

    boolean isLightPopulated();

    boolean isLoaded();

    boolean isPopulated();

    boolean load() throws IOException;

    boolean load(boolean generate) throws IOException;

    void populateSkyLight();

    void recalculateHeightMap();

    void removeBlockEntity(BlockEntity blockEntity);

    void removeEntity(Entity entity);

    default void setBiome(int x, int z, cn.nukkit.level.biome.Biome biome) {
        setBiomeId(x, z, biome.getId());
    }

    @Deprecated
    default void setBiomeColor(int x, int z, int r, int g, int b) {
    }

    default void setBiomeId(int x, int y, int z, int biomeId) {
        this.setBiomeId(x, y, z, (byte) biomeId);
    }

    default void setBiomeId(int x, int z, int biomeId) {
        setBiomeId(x, z, (byte) biomeId);
    }

    default void setBiomeId(int x, int y, int z, byte biomeId) {
        this.setBiomeId(x, z, biomeId);
    }

    void setBiomeId(int x, int z, byte biomeId);

    @Deprecated
    default void setBiomeIdAndColor(int x, int z, int idAndColor) {
    }

    void setBiomeIdArray(byte[] biomeIdArray);

    boolean setBlock(int x, int y, int z, int blockId);

    boolean setBlock(int x, int y, int z, int blockId, int meta);

    boolean setBlockAtLayer(int x, int y, int z, BlockLayer layer, int id);

    boolean setBlockAtLayer(int x, int y, int z, BlockLayer layer, int id, int data);

    default void setBlockData(int x, int y, int z, int data) {
        this.setBlockData(x, y, z, Block.LAYER_NORMAL, data);
    }

    void setBlockData(int x, int y, int z, BlockLayer layer, int data);

    void setBlockExtraData(int x, int y, int z, int data);

    default void setBlockId(int x, int y, int z, int id) {
        this.setBlockId(x, y, z, Block.LAYER_NORMAL, id);
    }

    void setBlockId(int x, int y, int z, BlockLayer layer, int id);

    void setBlockLight(int x, int y, int z, int level);

    void setBlockSkyLight(int x, int y, int z, int level);

    void setChanged();

    void setChanged(boolean changed);

    default boolean setFullBlockId(int x, int y, int z, int fullId) {
        return setFullBlockId(x, y, z, Block.LAYER_NORMAL, fullId);
    }

    default boolean setFullBlockId(int x, int y, int z, BlockLayer layer, int fullId) {
        return setBlockAtLayer(x, y, z, layer, fullId >> Block.DATA_BITS, fullId & Block.DATA_MASK);
    }

    void setGenerated();

    void setGenerated(boolean value);

    void setHeightMap(int x, int z, int value);

    void setLightPopulated();

    void setLightPopulated(boolean value);

    void setPopulated();

    void setPopulated(boolean value);

    default void setPosition(int x, int z) {
        setX(x);
        setZ(z);
    }

    void setProvider(LevelProvider provider);

    void setX(int x);

    void setZ(int z);

    byte[] toBinary();

    @Deprecated
    byte[] toFastBinary();

    boolean unload() throws Exception;

    boolean unload(boolean save) throws Exception;

    boolean unload(boolean save, boolean safe) throws Exception;
}