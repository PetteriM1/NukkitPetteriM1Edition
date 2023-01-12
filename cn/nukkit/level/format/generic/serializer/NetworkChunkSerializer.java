/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.generic.serializer;

import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.level.DimensionData;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.format.anvil.Anvil;
import cn.nukkit.level.format.generic.BaseChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.format.generic.serializer.NetworkChunkData;
import cn.nukkit.level.util.PalettedBlockStorage;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.ThreadCache;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.function.BiConsumer;

public class NetworkChunkSerializer {
    private static final int b = 4;
    private static final byte[] a;

    public static void serialize(BaseChunk baseChunk, IntSet intSet, BiConsumer<BinaryStream, NetworkChunkData> biConsumer, boolean bl, DimensionData dimensionData) {
        NetworkChunkSerializer.a(baseChunk, intSet, biConsumer, bl, dimensionData);
    }

    private static void a(BaseChunk baseChunk, IntSet intSet, BiConsumer<BinaryStream, NetworkChunkData> biConsumer, boolean bl, DimensionData dimensionData) {
        byte[] byArray = baseChunk.getBlockEntities().isEmpty() ? new byte[]{} : NetworkChunkSerializer.a(baseChunk);
        int n = 0;
        ChunkSection[] chunkSectionArray = baseChunk.getSections();
        for (int k = chunkSectionArray.length - 1; k >= 0; --k) {
            if (chunkSectionArray[k].isEmpty()) continue;
            n = k + 1;
            break;
        }
        IntIterator intIterator = intSet.iterator();
        while (intIterator.hasNext()) {
            int n2 = (Integer)intIterator.next();
            BinaryStream binaryStream = ThreadCache.binaryStream.get().reset();
            NetworkChunkData networkChunkData = new NetworkChunkData(n2, n, bl, dimensionData);
            if (n2 >= 503) {
                NetworkChunkSerializer.d(binaryStream, baseChunk, chunkSectionArray, networkChunkData, byArray);
            } else if (n2 >= 474) {
                NetworkChunkSerializer.b(binaryStream, baseChunk, chunkSectionArray, networkChunkData, byArray);
            } else if (n2 >= 388) {
                NetworkChunkSerializer.a(binaryStream, baseChunk, chunkSectionArray, networkChunkData, byArray);
            } else {
                NetworkChunkSerializer.c(binaryStream, baseChunk, chunkSectionArray, networkChunkData, byArray);
            }
            biConsumer.accept(binaryStream, networkChunkData);
        }
    }

    private static void d(BinaryStream binaryStream, BaseFullChunk baseFullChunk, ChunkSection[] chunkSectionArray, NetworkChunkData networkChunkData, byte[] byArray) {
        DimensionData dimensionData = networkChunkData.getDimensionData();
        int n = dimensionData.getHeight() >> 4;
        int n2 = Math.min(n, networkChunkData.getChunkSections());
        byte[] byArray2 = NetworkChunkSerializer.a(baseFullChunk, n, Server.getInstance().suomiCraftPEMode() && networkChunkData.getDimensionData().getDimensionId() == 2);
        binaryStream.reset();
        int n3 = n2;
        if (dimensionData.getDimensionId() == 0 && n2 < n) {
            binaryStream.put(a);
            n3 += 4;
        }
        for (int k = 0; k < n2; ++k) {
            chunkSectionArray[k].writeTo(networkChunkData.getProtocol(), binaryStream, networkChunkData.isAntiXray());
        }
        binaryStream.put(byArray2);
        binaryStream.putByte((byte)0);
        binaryStream.put(byArray);
        networkChunkData.setChunkSections(n3);
    }

    private static void b(BinaryStream binaryStream, BaseFullChunk baseFullChunk, ChunkSection[] chunkSectionArray, NetworkChunkData networkChunkData, byte[] byArray) {
        boolean bl = networkChunkData.getDimensionData().getDimensionId() == 0;
        byte[] byArray2 = NetworkChunkSerializer.a(baseFullChunk, 25, Server.getInstance().suomiCraftPEMode() && networkChunkData.getDimensionData().getDimensionId() == 2);
        binaryStream.reset();
        if (bl) {
            binaryStream.put(a);
        }
        for (int k = 0; k < networkChunkData.getChunkSections(); ++k) {
            chunkSectionArray[k].writeTo(networkChunkData.getProtocol(), binaryStream, networkChunkData.isAntiXray());
        }
        binaryStream.put(byArray2);
        binaryStream.putByte((byte)0);
        binaryStream.put(byArray);
        networkChunkData.setChunkSections((bl ? 4 : 0) + networkChunkData.getChunkSections());
    }

    private static void a(BinaryStream binaryStream, BaseFullChunk baseFullChunk, ChunkSection[] chunkSectionArray, NetworkChunkData networkChunkData, byte[] byArray) {
        for (int k = 0; k < networkChunkData.getChunkSections(); ++k) {
            chunkSectionArray[k].writeTo(networkChunkData.getProtocol(), binaryStream, networkChunkData.isAntiXray());
        }
        binaryStream.put(baseFullChunk.getBiomeIdArray());
        binaryStream.putByte((byte)0);
        if (networkChunkData.getProtocol() < 419) {
            binaryStream.putVarInt(0);
        }
        binaryStream.put(byArray);
    }

    private static void c(BinaryStream binaryStream, BaseFullChunk baseFullChunk, ChunkSection[] chunkSectionArray, NetworkChunkData networkChunkData, byte[] byArray) {
        if (networkChunkData.getProtocol() < 361) {
            binaryStream.putByte((byte)networkChunkData.getChunkSections());
        }
        for (int k = 0; k < networkChunkData.getChunkSections(); ++k) {
            binaryStream.putByte((byte)0);
            binaryStream.put(chunkSectionArray[k].getBytes(networkChunkData.isAntiXray()));
        }
        if (networkChunkData.getProtocol() < 361) {
            for (byte by : baseFullChunk.getHeightMapArray()) {
                binaryStream.putByte(by);
            }
            binaryStream.put(Anvil.PAD_256);
        }
        binaryStream.put(baseFullChunk.getBiomeIdArray());
        binaryStream.putByte((byte)0);
        binaryStream.putVarInt(0);
        binaryStream.put(byArray);
    }

    private static byte[] a(BaseChunk baseChunk) {
        ObjectArrayList<CompoundTag> objectArrayList = new ObjectArrayList<CompoundTag>();
        for (BlockEntity blockEntity : baseChunk.getBlockEntities().values()) {
            if (!(blockEntity instanceof BlockEntitySpawnable)) continue;
            objectArrayList.add(((BlockEntitySpawnable)blockEntity).getSpawnCompound());
        }
        try {
            return NBTIO.write(objectArrayList, ByteOrder.LITTLE_ENDIAN, true);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    private static byte[] a(BaseFullChunk baseFullChunk, int n, boolean bl) {
        int n2;
        PalettedBlockStorage palettedBlockStorage = PalettedBlockStorage.createWithDefaultState(Biome.getBiomeIdOrCorrect(baseFullChunk.getBiomeId(0, 0)));
        for (int k = 0; k < 16; ++k) {
            for (int i2 = 0; i2 < 16; ++i2) {
                n2 = bl ? EnumBiome.END.id : Biome.getBiomeIdOrCorrect(baseFullChunk.getBiomeId(k, i2));
                for (int i3 = 0; i3 < 16; ++i3) {
                    palettedBlockStorage.setBlock(k, i3, i2, n2);
                }
            }
        }
        BinaryStream binaryStream = ThreadCache.binaryStream.get().reset();
        palettedBlockStorage.writeTo(binaryStream);
        byte[] byArray = binaryStream.getBuffer();
        binaryStream.reset();
        for (n2 = 0; n2 < n; ++n2) {
            binaryStream.put(byArray);
        }
        return binaryStream.getBuffer();
    }

    static {
        BinaryStream binaryStream = new BinaryStream();
        for (int k = 0; k < 4; ++k) {
            binaryStream.putByte((byte)8);
            binaryStream.putByte((byte)0);
        }
        a = binaryStream.getBuffer();
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

