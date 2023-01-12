/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.i;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.anvil.Anvil;
import cn.nukkit.level.format.anvil.palette.BiomePalette;
import cn.nukkit.level.format.generic.BaseChunk;
import cn.nukkit.level.format.generic.EmptyChunkSection;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.ByteArrayTag;
import cn.nukkit.nbt.tag.ByteTag;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.LongTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.BlockUpdateEntry;
import cn.nukkit.utils.ChunkException;
import cn.nukkit.utils.Zlib;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Chunk
extends BaseChunk {
    protected long inhabitedTime;
    protected boolean terrainPopulated;
    protected boolean terrainGenerated;

    @Override
    public Chunk clone() {
        return (Chunk)super.clone();
    }

    @Override
    protected Chunk fullClone() {
        return (Chunk)super.fullClone();
    }

    public Chunk(LevelProvider levelProvider) {
        this(levelProvider, null);
    }

    public Chunk(Class<? extends LevelProvider> clazz) {
        this((LevelProvider)null, null);
        this.providerClass = clazz;
    }

    public Chunk(Class<? extends LevelProvider> clazz, CompoundTag compoundTag) {
        this((LevelProvider)null, compoundTag);
        this.providerClass = clazz;
    }

    public Chunk(LevelProvider levelProvider, CompoundTag compoundTag) {
        ListTag<CompoundTag> listTag;
        int n;
        int biomePalette;
        this.provider = levelProvider;
        if (levelProvider != null) {
            this.providerClass = levelProvider.getClass();
        }
        if (compoundTag == null) {
            this.biomes = new byte[256];
            this.sections = new ChunkSection[16];
            System.arraycopy(EmptyChunkSection.EMPTY, 0, this.sections, 0, 16);
            return;
        }
        this.sections = new ChunkSection[16];
        for (Tag tag2 : compoundTag.getList("Sections").getAll()) {
            int binaryStream;
            if (!(tag2 instanceof CompoundTag) || (binaryStream = ((CompoundTag)tag2).getByte("Y")) >= 16) continue;
            this.sections[binaryStream] = new cn.nukkit.level.format.anvil.ChunkSection((CompoundTag)tag2);
        }
        for (int k = 0; k < 16; ++k) {
            if (this.sections[k] != null) continue;
            this.sections[k] = EmptyChunkSection.EMPTY[k];
        }
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        Tag tag = compoundTag.get("ExtraData");
        if (tag instanceof ByteArrayTag) {
            BinaryStream nArray = new BinaryStream(((ByteArrayTag)tag).data);
            for (biomePalette = 0; biomePalette < nArray.getInt(); ++biomePalette) {
                n = nArray.getInt();
                hashMap.put(n, nArray.getShort());
            }
        }
        this.setPosition(compoundTag.getInt("xPos"), compoundTag.getInt("zPos"));
        if (this.sections.length > 16) {
            throw new ChunkException("Invalid amount of chunks: " + this.sections.length);
        }
        if (compoundTag.contains("BiomeColors")) {
            this.biomes = new byte[256];
            int[] nArray = compoundTag.getIntArray("BiomeColors");
            if (nArray != null && nArray.length == 256) {
                BiomePalette listTag2 = new BiomePalette(nArray);
                for (n = 0; n < 16; ++n) {
                    for (int k = 0; k < 16; ++k) {
                        this.biomes[n << 4 | k] = (byte)(listTag2.get(n, k) >> 24);
                    }
                }
            }
        } else {
            this.biomes = Arrays.copyOf(compoundTag.getByteArray("Biomes"), 256);
        }
        int[] nArray = compoundTag.getIntArray("HeightMap");
        this.heightMap = new byte[256];
        if (nArray.length != 256) {
            Arrays.fill(this.heightMap, (byte)-1);
        } else {
            for (biomePalette = 0; biomePalette < nArray.length; ++biomePalette) {
                this.heightMap[biomePalette] = (byte)nArray[biomePalette];
            }
        }
        if (!hashMap.isEmpty()) {
            this.extraData = hashMap;
        }
        this.NBTentities = compoundTag.getList("Entities", CompoundTag.class).getAll();
        this.NBTtiles = compoundTag.getList("TileEntities", CompoundTag.class).getAll();
        if (this.NBTentities.isEmpty()) {
            this.NBTentities = null;
        }
        if (this.NBTtiles.isEmpty()) {
            this.NBTtiles = null;
        }
        if ((listTag = compoundTag.getList("TileTicks", CompoundTag.class)) != null && listTag.size() > 0 && listTag.size() < 10000) {
            for (CompoundTag compoundTag2 : listTag.getAll()) {
                Block block;
                block22: {
                    block = null;
                    try {
                        Tag throwable = compoundTag2.get("i");
                        if (!(throwable instanceof StringTag)) break block22;
                        String string = ((StringTag)throwable).data;
                        Class<?> clazz = Class.forName(i.a("cn.nukkit.block." + string));
                        Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[0]);
                        constructor.setAccessible(true);
                        block = (Block)constructor.newInstance(new Object[0]);
                    }
                    catch (Throwable throwable) {
                        continue;
                    }
                }
                if (block == null) continue;
                block.x = compoundTag2.getInt("x");
                block.y = compoundTag2.getInt("y");
                block.z = compoundTag2.getInt("z");
                this.provider.getLevel().scheduleUpdate(block, block, compoundTag2.getInt("t"), compoundTag2.getInt("p"), false);
            }
        }
        this.inhabitedTime = compoundTag.getLong("InhabitedTime");
        this.terrainPopulated = compoundTag.getBoolean("TerrainPopulated");
        this.terrainGenerated = compoundTag.getBoolean("TerrainGenerated");
    }

    @Override
    public boolean isPopulated() {
        return this.terrainPopulated;
    }

    @Override
    public void setPopulated() {
        this.setPopulated(true);
    }

    @Override
    public void setPopulated(boolean bl) {
        block0: {
            if (bl == this.terrainPopulated) break block0;
            this.terrainPopulated = bl;
            this.setChanged();
        }
    }

    @Override
    public boolean isGenerated() {
        return this.terrainGenerated || this.terrainPopulated;
    }

    @Override
    public void setGenerated() {
        this.setGenerated(true);
    }

    @Override
    public void setGenerated(boolean bl) {
        block0: {
            if (this.terrainGenerated == bl) break block0;
            this.terrainGenerated = bl;
            this.setChanged();
        }
    }

    public CompoundTag getNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("LightPopulated", new ByteTag("LightPopulated", (byte)(this.isLightPopulated() ? 1 : 0)));
        compoundTag.put("InhabitedTime", new LongTag("InhabitedTime", this.inhabitedTime));
        compoundTag.put("V", new ByteTag("V", 1));
        compoundTag.put("TerrainGenerated", new ByteTag("TerrainGenerated", (byte)(this.isGenerated() ? 1 : 0)));
        compoundTag.put("TerrainPopulated", new ByteTag("TerrainPopulated", (byte)(this.terrainPopulated ? 1 : 0)));
        return compoundTag;
    }

    public static Chunk fromBinary(byte[] byArray) {
        return Chunk.fromBinary(byArray, null);
    }

    public static Chunk fromBinary(byte[] byArray, LevelProvider levelProvider) {
        try {
            CompoundTag compoundTag = NBTIO.read(new ByteArrayInputStream(Zlib.inflate(byArray)), ByteOrder.BIG_ENDIAN);
            if (!compoundTag.contains("Level") || !(compoundTag.get("Level") instanceof CompoundTag)) {
                return null;
            }
            return new Chunk(levelProvider, compoundTag.getCompound("Level"));
        }
        catch (Exception exception) {
            Server.getInstance().getLogger().logException(exception);
            return null;
        }
    }

    public static Chunk fromFastBinary(byte[] byArray) {
        return Chunk.fromFastBinary(byArray, null);
    }

    public static Chunk fromFastBinary(byte[] byArray, LevelProvider levelProvider) {
        try {
            CompoundTag compoundTag = NBTIO.read(new DataInputStream(new ByteArrayInputStream(byArray)), ByteOrder.BIG_ENDIAN);
            if (!compoundTag.contains("Level") || !(compoundTag.get("Level") instanceof CompoundTag)) {
                return null;
            }
            return new Chunk(levelProvider, compoundTag.getCompound("Level"));
        }
        catch (Exception exception) {
            return null;
        }
    }

    @Override
    public byte[] toFastBinary() {
        Object object;
        CompoundTag compoundTag = this.getNBT().copy();
        compoundTag.remove("BiomeColors");
        compoundTag.putInt("xPos", this.getX());
        compoundTag.putInt("zPos", this.getZ());
        compoundTag.putByteArray("Biomes", this.getBiomeIdArray());
        for (ChunkSection object32 : this.getSections()) {
            if (object32 instanceof EmptyChunkSection) continue;
            CompoundTag compoundTag2 = new CompoundTag();
            compoundTag2.putByte("Y", object32.getY());
            compoundTag2.putByteArray("Blocks", object32.getIdArray(1));
            compoundTag2.putByteArray("Blocks2PM1E", object32.getIdArray(2));
            compoundTag2.putByteArray("Data", object32.getDataArray());
            compoundTag2.putByteArray("BlockLight", object32.getLightArray());
            compoundTag2.putByteArray("SkyLight", object32.getSkyLightArray());
            compoundTag.getList("Sections", CompoundTag.class).add(compoundTag2);
        }
        ArrayList arrayList = new ArrayList();
        for (Entity entity : this.getEntities().values()) {
            if (entity instanceof Player || entity.closed) continue;
            entity.saveNBT();
            arrayList.add(entity.namedTag);
        }
        ListTag listTag = new ListTag("Entities");
        listTag.setAll(arrayList);
        compoundTag.putList(listTag);
        ArrayList<CompoundTag> arrayList2 = new ArrayList<CompoundTag>();
        for (BlockEntity blockEntity : this.getBlockEntities().values()) {
            blockEntity.saveNBT();
            arrayList2.add(blockEntity.namedTag);
        }
        ListTag<CompoundTag> listTag2 = new ListTag<CompoundTag>("TileEntities");
        listTag2.setAll(arrayList2);
        compoundTag.putList(listTag2);
        Set<BlockUpdateEntry> set = this.provider.getLevel().getPendingBlockUpdates(this);
        if (set != null) {
            object = new ListTag("TileTicks");
            long l = this.provider.getLevel().getCurrentTick();
            for (BlockUpdateEntry blockUpdateEntry : set) {
                CompoundTag compoundTag3 = new CompoundTag().putString("i", blockUpdateEntry.block.getSaveId()).putInt("x", blockUpdateEntry.pos.getFloorX()).putInt("y", blockUpdateEntry.pos.getFloorY()).putInt("z", blockUpdateEntry.pos.getFloorZ()).putInt("t", (int)(blockUpdateEntry.delay - l)).putInt("p", blockUpdateEntry.priority);
                ((ListTag)object).add(compoundTag3);
            }
            compoundTag.putList((ListTag<? extends Tag>)object);
        }
        object = new BinaryStream();
        Map<Integer, Integer> map = this.getBlockExtraDataArray();
        ((BinaryStream)object).putInt(map.size());
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            ((BinaryStream)object).putInt(entry.getKey());
            ((BinaryStream)object).putShort(entry.getValue());
        }
        compoundTag.putByteArray("ExtraData", ((BinaryStream)object).getBuffer());
        CompoundTag compoundTag4 = new CompoundTag("");
        compoundTag4.putCompound("Level", compoundTag);
        try {
            return NBTIO.write(compoundTag4, ByteOrder.BIG_ENDIAN);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    @Override
    public byte[] toBinary() {
        Object object;
        int arrayList;
        CompoundTag compoundTag = this.getNBT().copy();
        compoundTag.remove("BiomeColors");
        compoundTag.putInt("xPos", this.getX());
        compoundTag.putInt("zPos", this.getZ());
        ListTag<CompoundTag> listTag = new ListTag<CompoundTag>("Sections");
        ChunkSection[] objectArray = this.getSections();
        int byArray = objectArray.length;
        for (arrayList = 0; arrayList < byArray; ++arrayList) {
            ChunkSection chunkSection = objectArray[arrayList];
            if (chunkSection instanceof EmptyChunkSection) continue;
            CompoundTag compoundTag2 = new CompoundTag();
            compoundTag2.putByte("Y", chunkSection.getY());
            compoundTag2.putByteArray("Blocks", chunkSection.getIdArray(1));
            compoundTag2.putByteArray("Blocks2PM1E", chunkSection.getIdArray(2));
            compoundTag2.putByteArray("Data", chunkSection.getDataArray());
            compoundTag2.putByteArray("BlockLight", chunkSection.getLightArray());
            compoundTag2.putByteArray("SkyLight", chunkSection.getSkyLightArray());
            listTag.add(compoundTag2);
        }
        compoundTag.putList(listTag);
        compoundTag.putByteArray("Biomes", this.getBiomeIdArray());
        int[] nArray = new int[256];
        byte[] byArray2 = this.getHeightMapArray();
        for (arrayList = 0; arrayList < nArray.length; ++arrayList) {
            nArray[arrayList] = byArray2[arrayList] & 0xFF;
        }
        compoundTag.putIntArray("HeightMap", nArray);
        ArrayList<CompoundTag> arrayList2 = new ArrayList<CompoundTag>();
        for (Entity entity : this.getEntities().values()) {
            if (entity instanceof Player || entity.closed) continue;
            entity.saveNBT();
            arrayList2.add(entity.namedTag);
        }
        ListTag<CompoundTag> listTag2 = new ListTag<CompoundTag>("Entities");
        listTag2.setAll(arrayList2);
        compoundTag.putList(listTag2);
        ArrayList<CompoundTag> arrayList3 = new ArrayList<CompoundTag>();
        for (BlockEntity blockEntity : this.getBlockEntities().values()) {
            blockEntity.saveNBT();
            arrayList3.add(blockEntity.namedTag);
        }
        ListTag listTag3 = new ListTag("TileEntities");
        listTag3.setAll(arrayList3);
        compoundTag.putList(listTag3);
        Set<BlockUpdateEntry> set = this.provider.getLevel().getPendingBlockUpdates(this);
        if (set != null) {
            object = new ListTag("TileTicks");
            long l = this.provider.getLevel().getCurrentTick();
            for (BlockUpdateEntry blockUpdateEntry : set) {
                CompoundTag compoundTag3 = new CompoundTag().putString("i", blockUpdateEntry.block.getSaveId()).putInt("x", blockUpdateEntry.pos.getFloorX()).putInt("y", blockUpdateEntry.pos.getFloorY()).putInt("z", blockUpdateEntry.pos.getFloorZ()).putInt("t", (int)(blockUpdateEntry.delay - l)).putInt("p", blockUpdateEntry.priority);
                ((ListTag)object).add(compoundTag3);
            }
            compoundTag.putList((ListTag<? extends Tag>)object);
        }
        object = new BinaryStream();
        Map<Integer, Integer> map = this.getBlockExtraDataArray();
        ((BinaryStream)object).putInt(map.size());
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            ((BinaryStream)object).putInt(entry.getKey());
            ((BinaryStream)object).putShort(entry.getValue());
        }
        compoundTag.putByteArray("ExtraData", ((BinaryStream)object).getBuffer());
        CompoundTag compoundTag4 = new CompoundTag("");
        compoundTag4.putCompound("Level", compoundTag);
        try {
            return Zlib.deflate(NBTIO.write(compoundTag4, ByteOrder.BIG_ENDIAN), Server.getInstance().chunkCompressionLevel);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public int getBlockSkyLight(int n, int n2, int n3) {
        ChunkSection chunkSection = this.sections[n2 >> 4];
        if (chunkSection instanceof cn.nukkit.level.format.anvil.ChunkSection) {
            cn.nukkit.level.format.anvil.ChunkSection chunkSection2 = (cn.nukkit.level.format.anvil.ChunkSection)chunkSection;
            if (chunkSection2.skyLight != null) {
                return chunkSection.getBlockSkyLight(n, n2 & 0xF, n3);
            }
            if (!chunkSection2.hasSkyLight) {
                return 0;
            }
            int n4 = this.getHighestBlockAt(n, n3);
            if (n4 < n2) {
                return 15;
            }
            if (n4 == n2) {
                return Block.transparent[this.getBlockId(n, n2, n3)] ? 15 : 0;
            }
            return chunkSection.getBlockSkyLight(n, n2 & 0xF, n3);
        }
        return chunkSection.getBlockSkyLight(n, n2 & 0xF, n3);
    }

    @Override
    public int getBlockLight(int n, int n2, int n3) {
        ChunkSection chunkSection = this.sections[n2 >> 4];
        if (chunkSection instanceof cn.nukkit.level.format.anvil.ChunkSection) {
            cn.nukkit.level.format.anvil.ChunkSection chunkSection2 = (cn.nukkit.level.format.anvil.ChunkSection)chunkSection;
            if (chunkSection2.blockLight != null) {
                return chunkSection.getBlockLight(n, n2 & 0xF, n3);
            }
            if (!chunkSection2.hasBlockLight) {
                return 0;
            }
            return chunkSection.getBlockLight(n, n2 & 0xF, n3);
        }
        return chunkSection.getBlockLight(n, n2 & 0xF, n3);
    }

    public static Chunk getEmptyChunk(int n, int n2) {
        return Chunk.getEmptyChunk(n, n2, null);
    }

    public static Chunk getEmptyChunk(int n, int n2, LevelProvider levelProvider) {
        try {
            Chunk chunk = levelProvider != null ? new Chunk(levelProvider, null) : new Chunk(Anvil.class, null);
            chunk.setPosition(n, n2);
            chunk.heightMap = new byte[256];
            chunk.inhabitedTime = 0L;
            chunk.terrainGenerated = false;
            chunk.terrainPopulated = false;
            return chunk;
        }
        catch (Exception exception) {
            return null;
        }
    }

    @Override
    public boolean compress() {
        super.compress();
        boolean bl = false;
        for (ChunkSection chunkSection : this.getSections()) {
            cn.nukkit.level.format.anvil.ChunkSection chunkSection2;
            if (!(chunkSection instanceof cn.nukkit.level.format.anvil.ChunkSection) || (chunkSection2 = (cn.nukkit.level.format.anvil.ChunkSection)chunkSection).isEmpty()) continue;
            bl |= chunkSection2.compress();
        }
        return bl;
    }

    public String toString() {
        return "(Anvil) Chunk[" + this.getX() + "," + this.getZ() + "] (inhabitedTime=" + this.inhabitedTime + " terrainPopulated=" + this.terrainPopulated + " terrainGenerated=" + this.terrainGenerated + ')';
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}

