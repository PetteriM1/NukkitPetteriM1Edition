package cn.nukkit.level.format.generic;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.nbt.tag.CompoundTag;
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
    private static final PalettedBlockStorage EMPTY_STORAGE_PRE419 = new PalettedBlockStorage(BitArrayVersion.V1, 0);
    private static final PalettedBlockStorage EMPTY_STORAGE = new PalettedBlockStorage(BitArrayVersion.V1, ProtocolInfo.v1_16_100);

    private static final byte[] EMPTY_ID_ARRAY = new byte[4096];
    private static final byte[] EMPTY_DATA_ARRAY = new byte[2048];
    public static byte[] EMPTY_LIGHT_ARR = new byte[2048];
    public static byte[] EMPTY_SKY_LIGHT_ARR = new byte[2048];

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
    public int getY() {
        return this.y;
    }

    @Override
    final public int getBlockId(int x, int y, int z) {
        return 0;
    }

    @Override
    public int getBlockId(int x, int y, int z, int layer) {
        return 0;
    }

    @Override
    public int getFullBlock(int x, int y, int z) throws ChunkException {
        return 0;
    }

    @Override
    public int[] getBlockState(int x, int y, int z, int layer) {
        return new int[]{0,0};
    }

    @Override
    public boolean setBlockAtLayer(int x, int y, int z, int layer, int blockId) {
        if (blockId != 0) throw new ChunkException("Tried to modify an empty Chunk");
        return false;
    }

    @Override
    public Block getAndSetBlock(int x, int y, int z, int layer, Block block) {
        if (block.getId() != 0) throw new ChunkException("Tried to modify an empty Chunk");
        return Block.get(0);
    }

    @Override
    public Block getAndSetBlock(int x, int y, int z, Block block) {
        if (block.getId() != 0) throw new ChunkException("Tried to modify an empty Chunk");
        return Block.get(0);
    }

    @Override
    public void setBlockId(int x, int y, int z, int layer, int id) {
        if (id != 0) throw new ChunkException("Tried to modify an empty Chunk");
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
    public boolean setBlockAtLayer(int x, int y, int z, int layer, int blockId, int meta) {
        if (blockId != 0) throw new ChunkException("Tried to modify an empty Chunk");
        return false;
    }

    @Override
    public byte[] getIdArray() {
        return EMPTY_ID_ARRAY;
    }

    @Override
    public byte[] getIdExtraArray(int layer) {
        return EMPTY_ID_ARRAY;
    }

    @Override
    public byte[] getIdArray(int layer) {
        return EMPTY_ID_ARRAY;
    }

    @Override
    public byte[] getDataArray() {
        return EMPTY_DATA_ARRAY;
    }

    @Override
    public byte[] getDataArray(int layer) {
        return EMPTY_DATA_ARRAY;
    }

    @Override
    public byte[] getDataExtraArray(int layer) {
        return EMPTY_DATA_ARRAY;
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
    final public void setBlockId(int x, int y, int z, int id) throws ChunkException {
        if (id != 0) throw new ChunkException("Tried to modify an empty Chunk");
    }

    @Override
    public boolean setFullBlockId(int x, int y, int z, int layer, int fullId) {
        if (fullId != 0) throw new ChunkException("Tried to modify an empty Chunk");
        return false;
    }

    @Override
    final public int getBlockData(int x, int y, int z) {
        return 0;
    }

    @Override
    public int getBlockData(int x, int y, int z, int layer) {
        return 0;
    }

    @Override
    public void setBlockData(int x, int y, int z, int data) throws ChunkException {
        if (data != 0) throw new ChunkException("Tried to modify an empty Chunk");
    }

    @Override
    public void setBlockData(int x, int y, int z, int layer, int data) {
        if (data != 0) throw new ChunkException("Tried to modify an empty Chunk");
    }

    @Override
    public boolean setFullBlockId(int x, int y, int z, int fullId) {
        if (fullId != 0) throw new ChunkException("Tried to modify an empty Chunk");
        return false;
    }

    @Override
    public int getFullBlock(int x, int y, int z, int layer) {
        return 0;
    }

    @Override
    public int getBlockLight(int x, int y, int z) {
        return 0;
    }

    @Override
    public void setBlockLight(int x, int y, int z, int level) throws ChunkException {
        if (level != 0) throw new ChunkException("Tried to modify an empty Chunk");
    }

    @Override
    public int getBlockSkyLight(int x, int y, int z) {
        return 15;
    }

    @Override
    public void setBlockSkyLight(int x, int y, int z, int level) throws ChunkException {
        if (level != 15) throw new ChunkException("Tried to modify an empty Chunk");
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public byte[] getBytes(int protocolId) {
        return new byte[6145];
    }

    @Override
    public void writeTo(int protocol, BinaryStream stream) {
        stream.putByte((byte) 8);
        stream.putByte((byte) 2);
        if (protocol >= ProtocolInfo.v1_16_100) {
            EMPTY_STORAGE.writeTo(protocol, stream);
            EMPTY_STORAGE.writeTo(protocol, stream);
        } else {
            EMPTY_STORAGE_PRE419.writeTo(protocol, stream);
            EMPTY_STORAGE_PRE419.writeTo(protocol, stream);
        }
    }

    @Override
    public EmptyChunkSection copy() {
        return this;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}