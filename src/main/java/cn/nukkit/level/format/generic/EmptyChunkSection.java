package cn.nukkit.level.format.generic;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLayer;
import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.util.BitArrayVersion;
import cn.nukkit.level.util.PalettedBlockStorage;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.ChunkException;

import java.util.Arrays;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class EmptyChunkSection implements ChunkSection {

    public static final EmptyChunkSection[] EMPTY = new EmptyChunkSection[16];
    private static final PalettedBlockStorage EMPTY_STORAGE_PRE419 = PalettedBlockStorage.createFromBlockPalette(BitArrayVersion.V1, 0);
    private static final PalettedBlockStorage EMPTY_STORAGE = PalettedBlockStorage.createFromBlockPalette(BitArrayVersion.V1, ProtocolInfo.v1_16_100);

    public static final byte[] EMPTY_LIGHT_ARR = new byte[2048];
    public static final byte[] EMPTY_SKY_LIGHT_ARR = new byte[2048];
    private static final byte[] EMPTY_ID_ARR = new byte[4096];

    static {
        for (int y = 0; y < EMPTY.length; y++) {
            EMPTY[y] = new EmptyChunkSection(y);
        }

        Arrays.fill(EMPTY_SKY_LIGHT_ARR, (byte) 255);
    }

    private final int y;

    public EmptyChunkSection(int y) {
        this.y = y;
    }

    @Override
    public byte[] getDataArray() {
        return EMPTY_LIGHT_ARR; // empty 2048
    }

    @Override
    public byte[] getIdArray() {
        return this.getIdArray(1);
    }

    @Override
    public byte[] getLightArray() {
        return EMPTY_LIGHT_ARR;
    }

    @Override
    public byte[] getSkyLightArray() {
        return EMPTY_SKY_LIGHT_ARR;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public EmptyChunkSection copy() {
        return this;
    }

    @Override
    public EmptyChunkSection copyForChunkSending() {
        return this;
    }

    @Override
    public Block getAndSetBlock(int x, int y, int z, BlockLayer layer, Block block) {
        if (block.getId() != 0) throw new ChunkException("Tried to modify an empty Chunk");
        return Block.get(0);
    }

    @Override
    public int getBlockData(int x, int y, int z, BlockLayer layer) {
        return 0;
    }

    @Override
    public int getBlockId(int x, int y, int z, BlockLayer layer) {
        return 0;
    }

    @Override
    public int getBlockLight(int x, int y, int z) {
        return 0;
    }

    @Override
    public int getBlockSkyLight(int x, int y, int z) {
        return 15;
    }

    @Override
    public byte[] getBytes(boolean obfuscated) {
        return new byte[6144];
    }

    @Override
    public int getFullBlock(int x, int y, int z, BlockLayer layer) {
        return 0;
    }

    @Override
    public byte[] getIdArray(int ver) {
        return EMPTY_ID_ARR;
    }

    @Override
    public boolean setBlock(int x, int y, int z, int blockId) throws ChunkException {
        if (blockId != 0) throw new ChunkException("Tried to modify an empty Chunk");
        return false;
    }

    @Override
    public boolean setBlock(int x, int y, int z, int blockId, int meta) throws ChunkException {
        if (blockId != 0) throw new ChunkException("Tried to modify an empty Chunk");
        return false;
    }

    @Override
    public boolean setBlockAtLayer(int x, int y, int z, BlockLayer layer, int blockId) {
        if (blockId != 0) throw new ChunkException("Tried to modify an empty Chunk");
        return false;
    }

    @Override
    public boolean setBlockAtLayer(int x, int y, int z, BlockLayer layer, int blockId, int meta) {
        if (blockId != 0) throw new ChunkException("Tried to modify an empty Chunk");
        return false;
    }

    @Override
    public void setBlockData(int x, int y, int z, BlockLayer layer, int data) {
        if (data != 0) throw new ChunkException("Tried to modify an empty Chunk");
    }

    @Override
    public void setBlockId(int x, int y, int z, BlockLayer layer, int id) {
        if (id != 0) throw new ChunkException("Tried to modify an empty Chunk");
    }

    @Override
    public void setBlockLight(int x, int y, int z, int level) throws ChunkException {
        if (level != 0) throw new ChunkException("Tried to modify an empty Chunk");
    }

    @Override
    public void setBlockSkyLight(int x, int y, int z, int level) throws ChunkException {
        if (level != 15) throw new ChunkException("Tried to modify an empty Chunk");
    }

    @Override
    public boolean setFullBlockId(int x, int y, int z, BlockLayer layer, int fullId) {
        if (fullId != 0) throw new ChunkException("Tried to modify an empty Chunk");
        return false;
    }

    @Override
    public void writeTo(int protocol, BinaryStream stream, boolean obfuscated) {
        stream.putByte(protocol >= ProtocolInfo.v1_19_80 ? (byte) 9 : (byte) 8); // SubChunk version
        if (protocol >= ProtocolInfo.v1_18_30) {
            stream.putByte((byte) 0); // layers
            if (protocol >= ProtocolInfo.v1_19_80) {
                stream.putByte((byte) this.y);
            }
        } else {
            stream.putByte((byte) 1); // layers
            if (protocol >= ProtocolInfo.v1_16_100) {
                EMPTY_STORAGE.writeTo(stream);
            } else {
                EMPTY_STORAGE_PRE419.writeTo(stream);
            }
        }
    }
}
