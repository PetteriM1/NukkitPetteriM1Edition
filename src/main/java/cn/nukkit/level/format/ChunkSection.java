package cn.nukkit.level.format;

import cn.nukkit.block.Block;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.BinaryStream;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public interface ChunkSection {

    int getY();

    int getBlockId(int x, int y, int z);

    int getBlockId(int x, int y, int z, int layer);

    void setBlockId(int x, int y, int z, int id);

    boolean setFullBlockId(int x, int y, int z, int layer, int fullId);

    int getBlockData(int x, int y, int z);

    int getBlockData(int x, int y, int z, int layer);

    void setBlockData(int x, int y, int z, int data);

    void setBlockData(int x, int y, int z, int layer, int data);

    int getFullBlock(int x, int y, int z);

    default int[] getBlockState(int x, int y, int z) {
        return getBlockState(x, y, z, 0);
    }

    default int[] getBlockState(int x, int y, int z, int layer) {
        return new int[] {getBlockId(x, y, z, layer), getBlockData(x, y, z, layer)};
    }

    Block getAndSetBlock(int x, int y, int z, int layer, Block block);

    Block getAndSetBlock(int x, int y, int z, Block block);

    void setBlockId(int x, int y, int z, int layer, int id);

    boolean setFullBlockId(int x, int y, int z, int fullId);

    int getFullBlock(int x, int y, int z, int layer);

    boolean setBlockAtLayer(int x, int y, int z, int layer, int blockId);

    boolean setBlock(int x, int y, int z, int blockId);

    boolean setBlock(int x, int y, int z, int blockId, int meta);

    boolean setBlockAtLayer(int x, int y, int z, int layer, int blockId, int meta);

    int getBlockSkyLight(int x, int y, int z);

    void setBlockSkyLight(int x, int y, int z, int level);

    int getBlockLight(int x, int y, int z);

    void setBlockLight(int x, int y, int z, int level);

    byte[] getIdExtraArray(int layer);

    byte[] getIdArray(int layer);

    byte[] getIdArray();

    byte[] getDataArray();

    byte[] getDataArray(int layer);
    
    default byte[] getDataExtraArray() {
        return getDataExtraArray(0);
    }
    
    byte[] getDataExtraArray(int layer);

    default byte[][] getHyperDataArray() {
        return getHyperDataArray(0);
    }

    default byte[][] getHyperDataArray(int layer) {
        return new byte[0][];
    }

    byte[] getSkyLightArray();

    byte[] getLightArray();

    boolean isEmpty();

    // for < 1.13 chunk format
    byte[] getBytes(int protocolId);
    
    int getMaximumLayer();

    CompoundTag toNBT();

    // for >= 1.13 chunk format
    void writeTo(int protocol, BinaryStream stream);

    ChunkSection copy();

    default int getContentVersion() {
        return 0;
    }
}
