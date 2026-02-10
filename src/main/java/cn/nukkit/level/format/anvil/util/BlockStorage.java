package cn.nukkit.level.format.anvil.util;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.Level;
import cn.nukkit.level.util.PalettedBlockStorage;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;

import java.util.Arrays;

public class BlockStorage {

    private static final int SECTION_SIZE = 4096;
    private final byte[] blockIds;
    private final byte[] blockIds2;
    private final NibbleArray blockData;

    public BlockStorage() {
        blockIds = new byte[SECTION_SIZE];
        blockIds2 = new byte[SECTION_SIZE];
        blockData = new NibbleArray(SECTION_SIZE);
    }

    public BlockStorage(byte[] blockIds, byte[] blockIds2, NibbleArray blockData) {
        this.blockIds = blockIds;
        this.blockIds2 = blockIds2;
        this.blockData = blockData;
    }

    public BlockStorage copy() {
        return new BlockStorage(blockIds.clone(), blockIds2.clone(), blockData.copy());
    }

    public BlockStorage copyForChunkSending() {
        return new BlockStorage(blockIds, blockIds2, blockData);
    }

    public int getAndSetFullBlock(int x, int y, int z, int value) {
        return getAndSetFullBlock(getIndex(x, y, z), value);
    }

    private int getAndSetFullBlock(int index, int value) {
        // Convert to old data bits
        int block = value >> Block.DATA_BITS;
        int meta = value & Block.DATA_MASK;
        value = (block << 4) + Math.min(meta, 15);

        if (value >= 0x1fff) throw new IllegalArgumentException("Invalid full block " + value);
        int oldBlock = (blockIds2[index] & 0xff) + 255;
        if (oldBlock == 255) oldBlock = blockIds[index] & 0xff;
        byte oldData = blockData.get(index);
        byte newData = (byte) (value & 0xf);
        if (oldBlock != block) {
            if (block > 255) {
                blockIds[index] = (byte) 0;
                blockIds2[index] = (byte) ((block - 255) & 0xff);
            } else {
                blockIds[index] = (byte) (block & 0xff);
                blockIds2[index] = (byte) 0;
            }
        }
        if (oldData != newData) {
            blockData.set(index, newData);
        }

        // Convert to new data bits
        return (oldBlock << Block.DATA_BITS) | oldData;
    }

    public int getBlockData(int x, int y, int z) {
        return blockData.get(getIndex(x, y, z)) & 0xf;
    }

    public byte[] getBlockData() {
        return this.getBlockData(false);
    }

    public byte[] getBlockData(boolean copy) {
        if (copy) return Arrays.copyOf(blockData.getData(), blockData.getData().length);
        return blockData.getData();
    }

    public int getBlockId(int x, int y, int z) {
        int index = getIndex(x, y, z);
        int b = (blockIds2[index] & 0xff) + 255;
        if (b == 255) b = blockIds[index] & 0xff;
        return b;
    }

    public int getBlockIdFor(int x, int y, int z, int ver) {
        int index = getIndex(x, y, z);
        if (ver == 1) return blockIds[index] & 0xff;
        if (ver == 2) return blockIds2[index] & 0xff;
        throw new IllegalArgumentException("Unknown version");
    }

    public byte[] getBlockIds() {
        return this.getBlockIds(1);
    }

    public byte[] getBlockIds(int ver) {
        if (ver == 1) return Arrays.copyOf(blockIds, blockIds.length);
        if (ver == 2) return Arrays.copyOf(blockIds2, blockIds2.length);
        throw new IllegalArgumentException("Unknown version");
    }

    public int getFullBlock(int x, int y, int z) {
        return getFullBlock(getIndex(x, y, z));
    }

    private int getFullBlock(int index) {
        int b = (blockIds2[index] & 0xff) + 255;
        if (b == 255) b = blockIds[index] & 0xff;

        // Convert to new data bits
        return (b << Block.DATA_BITS) | blockData.get(index);
    }

    private static int getIndex(int x, int y, int z) {
        int index = (x << 8) + (z << 4) + y; // XZY = Bedrock format
        if (index < 0 || index >= SECTION_SIZE) throw new IllegalArgumentException("Invalid index");
        return index;
    }

    public void setBlockData(int x, int y, int z, int data) {
        blockData.set(getIndex(x, y, z), (byte) data);
    }

    public void setBlockId(int x, int y, int z, int id) {
        int index = getIndex(x, y, z);
        if (id > 255) {
            blockIds[index] = (byte) 0;
            blockIds2[index] = (byte) ((id - 255) & 0xff);
        } else {
            int value = id & 0xff;
            blockIds[index] = (byte) value;
            blockIds2[index] = (byte) 0;
            if (value == 0) {
                blockData.remove(index);
            }
        }
    }

    public void setFullBlock(int x, int y, int z, int value) {
        this.setFullBlock(getIndex(x, y, z), value);
    }

    private void setFullBlock(int index, int value) {
        // Convert to old data bits
        int block = value >> Block.DATA_BITS;
        int meta = value & Block.DATA_MASK;
        value = (block << 4) + Math.min(meta, 15);

        if (value >= 0x1fff) throw new IllegalArgumentException("Invalid full block " + value);
        byte data = (byte) (value & 0xf);
        if (block > 255) {
            blockIds[index] = (byte) 0;
            blockIds2[index] = (byte) ((block - 255) & 0xff);
        } else {
            blockIds[index] = (byte) (block & 0xff);
            blockIds2[index] = (byte) 0;
        }
        blockData.set(index, data);
    }

    public void writeTo(int protocol, int sectionY, BinaryStream stream, boolean obfuscated) {
        stream.putByte(protocol >= ProtocolInfo.v1_19_80 ? (byte) 9 : (byte) 8); // SubChunk version

        boolean secondLayerHasBlocks = false;
        PalettedBlockStorage layer1 = PalettedBlockStorage.createFromBlockPalette(protocol);
        PalettedBlockStorage layer2 = PalettedBlockStorage.createFromBlockPalette(protocol);
        int water = GlobalBlockPalette.getOrCreateRuntimeId(protocol, Block.WATER, 0);
        for (int i = 0; i < SECTION_SIZE; i++) {
            int id = (blockIds2[i] & 0xff) + 255;
            if (id == 255) id = blockIds[i] & 0xff;
            int data = blockData.get(i);
            if (obfuscated && Level.xrayableBlocks[id]) {
                id = BlockID.STONE;
                data = 0;
            }
            layer1.setBlock(i, GlobalBlockPalette.getOrCreateRuntimeId(protocol, id, data));
            // Hack: fake waterlogging
            if (id == BlockID.SEAGRASS || id == BlockID.BLOCK_KELP || id == BlockID.BUBBLE_COLUMN) {
                layer2.setBlock(i, water);

                secondLayerHasBlocks = true;
            }
        }

        stream.putByte((byte) (secondLayerHasBlocks ? 2 : 1)); // layers

        if (protocol >= ProtocolInfo.v1_19_80) {
            stream.putByte((byte) sectionY);
        }

        layer1.writeTo(stream);

        if (secondLayerHasBlocks) {
            layer2.writeTo(stream);
        }
    }
}
