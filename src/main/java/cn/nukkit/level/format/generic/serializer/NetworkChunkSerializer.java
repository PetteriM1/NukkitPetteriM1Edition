package cn.nukkit.level.format.generic.serializer;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.level.DimensionData;
import cn.nukkit.level.Level;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.format.generic.BaseChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.util.PalettedBlockStorage;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.ThreadCache;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.List;
import java.util.function.BiConsumer;

public class NetworkChunkSerializer {

    private static final int EXTENDED_NEGATIVE_SUB_CHUNKS = 4;

    private static final byte[] negativeSubChunksV8;
    private static final byte[] negativeSubChunksV9;

    static {
        // Build up 4 SubChunks for the extended negative height
        BinaryStream stream = new BinaryStream();
        for (int i = 0; i < EXTENDED_NEGATIVE_SUB_CHUNKS; i++) {
            stream.putByte((byte) 8); // SubChunk version
            stream.putByte((byte) 0); // 0 layers
        }
        negativeSubChunksV8 = stream.getBuffer();

        stream = new BinaryStream();
        for (int i = 0; i < EXTENDED_NEGATIVE_SUB_CHUNKS; i++) {
            stream.putByte((byte) 9); // SubChunk version
            stream.putByte((byte) 0); // 0 layers
            stream.putByte((byte) ((-64 + (i * 16)) / 16)); // section y
        }
        negativeSubChunksV9 = stream.getBuffer();
    }

    private static final byte[] PAD_256 = new byte[256];

    public static void serialize(BaseChunk chunk, IntSet protocols, BiConsumer<BinaryStream, NetworkChunkData> callback, boolean obfuscate, DimensionData dimensionData) {
        int subChunkCount = 0;
        ChunkSection[] sections = chunk.getSections();
        for (int i = sections.length - 1; i >= 0; i--) {
            if (!sections[i].isEmpty()) {
                subChunkCount = i + 1;
                break;
            }
        }

        for (int protocolId : protocols) {
            BinaryStream stream = ThreadCache.binaryStream.get().reset();
            NetworkChunkData chunkData = new NetworkChunkData(protocolId, subChunkCount, obfuscate, dimensionData);
            if (protocolId >= ProtocolInfo.v1_19_80) {
                serialize1_19_80(stream, chunk, sections, chunkData);
            } else if (protocolId >= ProtocolInfo.v1_18_30) {
                serialize1_18_30(stream, chunk, sections, chunkData);
            } else if (protocolId >= ProtocolInfo.v1_18_0_20) {
                serialize1_18(stream, chunk, sections, chunkData);
            } else if (protocolId >= ProtocolInfo.v1_13_0) {
                serialize1_13(stream, chunk, sections, chunkData);
            } else {
                serialize_legacy(stream, chunk, sections, chunkData);
            }

            byte[] blockEntities;
            if (chunk.getBlockEntities().isEmpty()) {
                blockEntities = new byte[0];
            } else {
                blockEntities = serializeEntities(chunk, protocolId);
            }
            stream.put(blockEntities);

            callback.accept(stream, chunkData);
        }
    }

    private static void serialize1_19_80(BinaryStream stream, BaseFullChunk chunk, ChunkSection[] sections, NetworkChunkData chunkData) {
        DimensionData dimensionData = chunkData.getDimensionData();
        int maxDimensionSections = dimensionData.getHeight() >> 4;
        int subChunkCount = Math.min(maxDimensionSections, chunkData.getChunkSections());

        byte[] biomePalettes = serialize3DBiomes(chunk, maxDimensionSections);
        stream.reset();

        // Overworld has negative coordinates, but we currently do not support them
        int writtenSections = subChunkCount;
        if (dimensionData.getDimensionId() == Level.DIMENSION_OVERWORLD && subChunkCount < maxDimensionSections) {
            stream.put(negativeSubChunksV9);
            writtenSections += EXTENDED_NEGATIVE_SUB_CHUNKS;
        }

        for (int i = 0; i < subChunkCount; i++) {
            sections[i].writeTo(chunkData.getProtocol(), stream, chunkData.isAntiXray());
        }

        stream.put(biomePalettes);
        stream.putByte((byte) 0); // Border blocks

        chunkData.setChunkSections(writtenSections);
    }

    private static void serialize1_18_30(BinaryStream stream, BaseFullChunk chunk, ChunkSection[] sections, NetworkChunkData chunkData) {
        DimensionData dimensionData = chunkData.getDimensionData();
        int maxDimensionSections = dimensionData.getHeight() >> 4;
        int subChunkCount = Math.min(maxDimensionSections, chunkData.getChunkSections());

        byte[] biomePalettes = serialize3DBiomes(chunk, maxDimensionSections);
        stream.reset();

        // Overworld has negative coordinates, but we currently do not support them
        int writtenSections = subChunkCount;
        if (dimensionData.getDimensionId() == Level.DIMENSION_OVERWORLD && subChunkCount < maxDimensionSections) {
            stream.put(negativeSubChunksV8);
            writtenSections += EXTENDED_NEGATIVE_SUB_CHUNKS;
        }

        for (int i = 0; i < subChunkCount; i++) {
            sections[i].writeTo(chunkData.getProtocol(), stream, chunkData.isAntiXray());
        }

        stream.put(biomePalettes);
        stream.putByte((byte) 0); // Border blocks

        chunkData.setChunkSections(writtenSections);
    }

    private static void serialize1_18(BinaryStream stream, BaseFullChunk chunk, ChunkSection[] sections, NetworkChunkData chunkData) {
        DimensionData dimensionData = chunkData.getDimensionData();
        int maxDimensionSections = dimensionData.getHeight() >> 4;
        int subChunkCount = Math.min(maxDimensionSections, chunkData.getChunkSections());

        byte[] biomePalettes = serialize3DBiomes(chunk, 25);
        stream.reset();

        // Overworld has negative coordinates, but we currently do not support them
        int writtenSections = subChunkCount;
        if (dimensionData.getDimensionId() == Level.DIMENSION_OVERWORLD && subChunkCount < maxDimensionSections) {
            stream.put(negativeSubChunksV8);
            writtenSections += EXTENDED_NEGATIVE_SUB_CHUNKS;
        }

        for (int i = 0; i < subChunkCount; i++) {
            sections[i].writeTo(chunkData.getProtocol(), stream, chunkData.isAntiXray());
        }

        stream.put(biomePalettes);
        stream.putByte((byte) 0); // Border blocks

        chunkData.setChunkSections(writtenSections);
    }

    private static void serialize1_13(BinaryStream stream, BaseFullChunk chunk, ChunkSection[] sections, NetworkChunkData chunkData) {
        int offset = chunkData.getDimensionData().getSectionOffset(); // Send 0 - 255
        for (int i = offset; i < Math.min(chunkData.getChunkSections() + offset, 16 + offset); i++) {
            sections[i].writeTo(chunkData.getProtocol(), stream, chunkData.isAntiXray());
        }

        stream.put(convert3DBiomesTo2D(chunk));
        stream.putByte((byte) 0); // Border blocks
        if (chunkData.getProtocol() < ProtocolInfo.v1_16_100) {
            stream.putVarInt(0); // There are no extra data anymore
        }

        // Send 0 - 255
        chunkData.setChunkSections(Math.min(chunkData.getChunkSections(), 16));
    }

    private static void serialize_legacy(BinaryStream stream, BaseFullChunk chunk, ChunkSection[] sections, NetworkChunkData chunkData) {
        if (chunkData.getProtocol() < ProtocolInfo.v1_12_0) {
            stream.putByte((byte) chunkData.getChunkSections());
        }

        int offset = chunkData.getDimensionData().getSectionOffset(); // Send 0 - 255
        for (int i = offset; i < Math.min(chunkData.getChunkSections() + offset, 16 + offset); i++) {
            stream.putByte((byte) 0);
            stream.put(sections[i].getBytes(chunkData.isAntiXray()));
        }

        if (chunkData.getProtocol() < ProtocolInfo.v1_12_0) {
            for (byte height : chunk.getHeightMapArray()) {
                stream.putByte(height);
            }
            stream.put(PAD_256);
        }

        stream.put(convert3DBiomesTo2D(chunk));
        stream.putByte((byte) 0); // Border blocks
        stream.putVarInt(0);

        // Send 0 - 255
        chunkData.setChunkSections(Math.min(chunkData.getChunkSections(), 16));
    }

    private static byte[] serializeEntities(BaseChunk chunk, int protocol) {
        List<CompoundTag> tagList = new ObjectArrayList<>();
        for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
            if (blockEntity instanceof BlockEntitySpawnable) {
                tagList.add(((BlockEntitySpawnable) blockEntity).getSpawnCompound(protocol));
            }
        }

        try {
            return NBTIO.write(tagList, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] serialize3DBiomes(BaseFullChunk chunk, int sections) {
        if (!chunk.has3dBiomes()) { // Convert 2D biomes to 3D
            PalettedBlockStorage palette = PalettedBlockStorage.createWithDefaultState(Biome.getBiomeIdOrCorrect(chunk.getBiomeId(0, 0)));
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int biomeId = chunk.getBiomeId(x, z);
                    for (int y = 0; y < 16; y++) {
                        palette.setBlock(x, y, z, biomeId);
                    }
                }
            }

            BinaryStream stream = ThreadCache.binaryStream.get().reset();
            palette.writeTo(stream, Biome::getBiomeIdOrCorrect);
            byte[] bytes = stream.getBuffer();
            stream.reset();

            for (int i = 0; i < sections; i++) {
                stream.put(bytes);
            }
            return stream.getBuffer();
        }

        BinaryStream stream = ThreadCache.binaryStream.get().reset();
        for (int i = 0; i < sections; i++) {
            PalettedBlockStorage storage = chunk.getBiomeStorage(i);
            storage.writeTo(stream, Biome::getBiomeIdOrCorrect);

        }
        return stream.getBuffer();
    }

    private static byte[] convert3DBiomesTo2D(BaseFullChunk chunk) {
        byte[] biomes = new byte[256];
        int i = 0;
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                biomes[i] = (byte) chunk.getBiomeId(x, z);
                i++;
            }
        }
        return biomes;
    }
}
