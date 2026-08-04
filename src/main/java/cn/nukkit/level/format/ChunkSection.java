package cn.nukkit.level.format;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLayer;
import cn.nukkit.utils.BinaryStream;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public interface ChunkSection {

    byte[] getDataArray();

    byte[] getIdArray();

    byte[] getLightArray();

    byte[] getSkyLightArray();

    int getY();

    boolean isEmpty();

    ChunkSection copy();

    ChunkSection copyForChunkSending();

    default Block getAndSetBlock(int x, int y, int z, Block block) {
        return this.getAndSetBlock(x, y, z, Block.LAYER_NORMAL, block);
    }

    Block getAndSetBlock(int x, int y, int z, BlockLayer layer, Block block);

    default int getBlockData(int x, int y, int z) {
        return this.getBlockData(x, y, z, Block.LAYER_NORMAL);
    }

    int getBlockData(int x, int y, int z, BlockLayer layer);

    default int getBlockId(int x, int y, int z) {
        return this.getBlockId(x, y, z, Block.LAYER_NORMAL);
    }

    int getBlockId(int x, int y, int z, BlockLayer layer);

    int getBlockLight(int x, int y, int z);

    int getBlockSkyLight(int x, int y, int z);

    // for < 1.13 chunk format
    byte[] getBytes(boolean obfuscated);

    default int getFullBlock(int x, int y, int z) {
        return this.getFullBlock(x, y, z, Block.LAYER_NORMAL);
    }

    int getFullBlock(int x, int y, int z, BlockLayer layer);

    // get block IDs for chunk save
    // ver 1 = id < 256, ver 2 = id < 512
    byte[] getIdArray(int ver);

    boolean setBlock(int x, int y, int z, int blockId);

    boolean setBlock(int x, int y, int z, int blockId, int meta);

    boolean setBlockAtLayer(int x, int y, int z, BlockLayer layer, int blockId);

    boolean setBlockAtLayer(int x, int y, int z, BlockLayer layer, int blockId, int meta);

    default void setBlockData(int x, int y, int z, int data) {
        this.setBlockData(x, y, z, Block.LAYER_NORMAL, data);
    }

    void setBlockData(int x, int y, int z, BlockLayer layer, int data);

    default void setBlockId(int x, int y, int z, int id) {
        this.setBlockId(x, y, z, Block.LAYER_NORMAL, id);
    }

    void setBlockId(int x, int y, int z, BlockLayer layer, int id);

    void setBlockLight(int x, int y, int z, int level);

    void setBlockSkyLight(int x, int y, int z, int level);

    default boolean setFullBlockId(int x, int y, int z, int fullId) {
        return this.setFullBlockId(x, y, z, Block.LAYER_NORMAL, fullId);
    }

    boolean setFullBlockId(int x, int y, int z, BlockLayer layer, int fullId);

    // for >= 1.13 chunk format
    void writeTo(int protocol, BinaryStream stream, boolean obfuscated);
}
