package cn.nukkit.level.format.leveldb.structure;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.leveldb.BlockStateMapping;
import cn.nukkit.level.util.BitArray;
import cn.nukkit.level.util.BitArrayVersion;
import cn.nukkit.level.util.PalettedBlockStorage;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.bugreport.ExceptionHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtUtils;

import java.io.IOException;
import java.util.List;

public class StateBlockStorage {
    private static final Logger log = LogManager.getLogger("LevelDB-Logger");

    private static final int SECTION_SIZE = 4096;

    private final List<BlockStateSnapshot> palette;
    private BitArray bitArray;

    public StateBlockStorage() {
        this(BitArrayVersion.V2);
    }

    public StateBlockStorage(BitArrayVersion version) {
        this.bitArray = version.createPalette();
        this.palette = new ObjectArrayList<>(16);
        // Air is at the beginning of each palette
        this.palette.add(BlockStateMapping.get().getState(0, 0));
    }

    public StateBlockStorage(BitArray bitArray, List<BlockStateSnapshot> palette) {
        this.palette = palette;
        this.bitArray = bitArray;
    }

    public byte[] getBlockData() {
        // TODO: possible to iterate over XZY 0-16
        Server.getInstance().getLogger().warning("Unsupported method getBlockData()! LevelDB provider is 1.13+", new Throwable(""));
        return new byte[0];
    }

    public byte[] getBlockIds() {
        // TODO: possible to iterate over XZY 0-16
        Server.getInstance().getLogger().warning("Unsupported method getBlockIds()! LevelDB provider is 1.13+", new Throwable(""));
        return new byte[0];
    }

    public boolean isEmpty() {
        if (this.palette.size() == 1) {
            return true;
        }
        for (int word : this.bitArray.getWords()) {
            if (Integer.toUnsignedLong(word) != 0L) {
                return false;
            }
        }
        return true;
    }

    public StateBlockStorage copy() {
        return new StateBlockStorage(this.bitArray.copy(), new ObjectArrayList<>(this.palette));
    }

    public int getAndSetFullBlock(int x, int y, int z, int value) {
        return getAndSetFullBlock(ChunkBuilder.getSectionIndex(x, y, z), value);
    }

    private int getAndSetFullBlock(int index, int value) {
        BlockStateSnapshot state = this.getBlockState(index);
        int newBlock = value >> Block.DATA_BITS;
        int newData = value & Block.DATA_MASK;
        this.setBlockStateUnsafe(index, BlockStateMapping.get().getState(newBlock, newData));
        return (state.getLegacyId() << Block.DATA_BITS) | state.getLegacyData();
    }

    public int getBlockData(int x, int y, int z) {
        return this.getBlockState(x, y, z).getLegacyData();
    }

    public int getBlockId(int x, int y, int z) {
        return this.getBlockState(x, y, z).getLegacyId();
    }

    public BlockStateSnapshot getBlockState(int x, int y, int z) {
        int index = ChunkBuilder.getSectionIndex(x, y, z);
        return this.stateFor(this.bitArray.get(index));
    }

    public BlockStateSnapshot getBlockState(int index) {
        return this.stateFor(this.bitArray.get(index));
    }

    public int getFullBlock(int x, int y, int z) {
        return getFullBlock(ChunkBuilder.getSectionIndex(x, y, z));
    }

    private int getFullBlock(int index) {
        BlockStateSnapshot state = this.getBlockState(index);
        return (state.getLegacyId() << Block.DATA_BITS) | state.getLegacyData();
    }

    private int getPaletteHeader(BitArrayVersion version, boolean runtime) {
        return (version.getId() << 1) | (runtime ? 1 : 0);
    }

    private static BitArrayVersion getVersionFromHeader(byte header) {
        return BitArrayVersion.get(header >> 1, true);
    }

    private int idFor(BlockStateSnapshot state) {
        int index = this.palette.indexOf(state);
        if (index != -1) {
            return index;
        }

        index = this.palette.size();
        BitArrayVersion version = this.bitArray.getVersion();
        if (index > version.getMaxEntryValue()) {
            BitArrayVersion next = version.next();
            if (next != null) {
                this.onResize(next);
            }
        }
        this.palette.add(state);
        return index;
    }

    private void onResize(BitArrayVersion version) {
        BitArray newBitArray = version.createPalette();
        for (int i = 0; i < SECTION_SIZE; i++) {
            newBitArray.set(i, this.bitArray.get(i));
        }
        this.bitArray = newBitArray;
    }

    public void readFromStorage(ByteBuf buffer, ChunkBuilder chunkBuilder) {
        BitArrayVersion version = getVersionFromHeader(buffer.readByte());
        this.palette.clear();
        int paletteSize = 1;

        if (version == BitArrayVersion.V0) {
            this.bitArray = version.createPalette(SECTION_SIZE, null);
        } else {
            int expectedWordCount = version.getWordsForSize(SECTION_SIZE);
            int[] words = new int[expectedWordCount];
            for (int i = 0; i < expectedWordCount; i++) {
                words[i] = buffer.readIntLE();
            }
            paletteSize = buffer.readIntLE();
            this.bitArray = version.createPalette(SECTION_SIZE, words);
        }

        if (version.getMaxEntryValue() < paletteSize - 1) {
            throw new IllegalArgumentException(
                    chunkBuilder.debugString() + " Palette (version " + version.name() + ") is too large. Max size " + version.getMaxEntryValue() + ". Actual size " + paletteSize
            );
        }

        try (ByteBufInputStream stream = new ByteBufInputStream(buffer);
             NBTInputStream nbtInputStream = NbtUtils.createReaderLE(stream)) {
            for (int i = 0; i < paletteSize; i++) {
                try {
                    NbtMap state = (NbtMap) nbtInputStream.readTag();
                    //noinspection ResultOfMethodCallIgnored
                    state.hashCode(); // cache hashCode

                    BlockStateSnapshot blockState = BlockStateMapping.get().getStateUnsafe(state);
                    if (blockState == null) {
                        NbtMap updatedState = BlockStateMapping.get().updateVanillaState(state);
                        blockState = BlockStateMapping.get().getUpdatedOrCustom(state, updatedState);
                        if (!blockState.isCustom()) {
                            if (Nukkit.DEBUG > 1)
                                log.info("[{}] Updated unmapped block state: {} => {}", chunkBuilder.debugString(), state, blockState.getVanillaState());
                            chunkBuilder.dirty();
                        }

                        if (Nukkit.DEBUG > 1 && blockState.getRuntimeId() == BlockStateMapping.get().getDefaultRuntimeId()) {
                            log.info("[{}] Chunk contains unknown block {} => {}", chunkBuilder.debugString(), state, updatedState);
                        }
                    }

                    if (Nukkit.DEBUG > 1 && this.palette.contains(blockState)) {
                        log.info("[{}] Palette contains block state twice: {}", chunkBuilder.debugString(), state);
                    }
                    this.palette.add(blockState);
                } catch (Exception e) {
                    MainLogger.getLogger().error("[" + chunkBuilder.debugString() + "] Unable to deserialize chunk block state", e);
                    ExceptionHandler.handleSilently(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBlockData(int x, int y, int z, int data) {
        int index = ChunkBuilder.getSectionIndex(x, y, z);
        this.setBlockStateUnsafe(index, BlockStateMapping.get().getState(this.getBlockId(x, y, z), data));
    }

    public void setBlockId(int x, int y, int z, int id) {
        int index = ChunkBuilder.getSectionIndex(x, y, z);
        this.setBlockStateUnsafe(index, BlockStateMapping.get().getState(id, 0));
    }

    public void setBlockState(int index, NbtMap state) {
        BlockStateSnapshot blockState = BlockStateMapping.get().getStateUnsafe(state);
        if (blockState == null) {
            blockState = BlockStateMapping.get().updateStateUnsafe(state);
        }
        this.setBlockStateUnsafe(index, blockState);
    }

    public void setBlockStateUnsafe(int index, BlockStateSnapshot state) {
        try {
            int id = this.idFor(state);
            this.bitArray.set(index, id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unable to set block state: " + state + ", palette: " + palette, e);
        }
    }

    public void setFullBlock(int x, int y, int z, int value) {
        this.setFullBlock(ChunkBuilder.getSectionIndex(x, y, z), value);
    }

    public void setFullBlock(int index, int value) {
        int block = value >> Block.DATA_BITS;
        int data = value & Block.DATA_MASK;
        this.setBlockStateUnsafe(index, BlockStateMapping.get().getState(block, data));
    }

    private BlockStateSnapshot stateFor(int index) {
        return this.palette.get(index);
    }

    public void writeTo(int protocol, BinaryStream stream, boolean obfuscated) {
        // If palette size is 1, it will have one state, no obfuscation needed
        if (obfuscated && this.palette.size() > 1) {
            this.writeToObfuscated(stream, protocol);
            return;
        }

        boolean legacy = protocol < ProtocolInfo.v1_18_0;

        BitArray bitArray;
        if (legacy && this.bitArray.getVersion() == BitArrayVersion.V0) {
            bitArray = BitArrayVersion.V1.createPalette(SECTION_SIZE);
        } else {
            bitArray = this.bitArray;
        }

        stream.putByte((byte) this.getPaletteHeader(bitArray.getVersion(), true));
        if (bitArray.getVersion() != BitArrayVersion.V0) {
            for (int word : bitArray.getWords()) {
                stream.putLInt(word);
            }
            stream.putVarInt(this.palette.size());
        }

        boolean mappingProtocol = protocol == Level.getChunkProtocol(BlockStateMapping.get().getVersion());

        for (BlockStateSnapshot state : this.palette) {
            if (mappingProtocol) {
                stream.putVarInt(state.getRuntimeId());
            } else {
                stream.putVarInt(GlobalBlockPalette.getOrCreateRuntimeId(protocol, state.getLegacyId(protocol), state.getLegacyData(protocol)));
            }
        }
    }

    private void writeToObfuscated(BinaryStream stream, int protocol) {
        PalettedBlockStorage storage = PalettedBlockStorage.createFromBlockPalette(this.bitArray.getVersion(), protocol);
        for (int i = 0; i < SECTION_SIZE; i++) {
            BlockStateSnapshot state = this.getBlockState(i);
            int id = state.getLegacyId(protocol);
            if (Level.xrayableBlocks[id]) {
                id = (id == BlockID.ANCIENT_DEBRIS || id == BlockID.NETHER_GOLD_ORE) ? BlockID.NETHERRACK : id > 650 ? BlockID.DEEPSLATE : BlockID.STONE;
            }
            storage.setBlock(i, GlobalBlockPalette.getOrCreateRuntimeId(protocol, id, state.getLegacyData(protocol)));
        }
        storage.writeTo(stream);
    }

    public void writeToStorage(ByteBuf buffer) {
        int paletteSize = this.palette.size();
        BitArrayVersion version = paletteSize <= 1 ? BitArrayVersion.V0 : this.bitArray.getVersion();
        buffer.writeByte(getPaletteHeader(version, false));

        if (version != BitArrayVersion.V0) {
            for (int word : this.bitArray.getWords()) {
                buffer.writeIntLE(word);
            }
            buffer.writeIntLE(paletteSize);
        }

        try (ByteBufOutputStream stream = new ByteBufOutputStream(buffer);
             NBTOutputStream nbtOutputStream = NbtUtils.createWriterLE(stream)) {
            for (BlockStateSnapshot state : this.palette) {
                nbtOutputStream.writeTag(state.getVanillaState());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
