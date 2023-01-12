/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.generic;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.NumberTag;
import cn.nukkit.network.protocol.BatchPacket;
import co.aikar.timings.Timing;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class BaseFullChunk
implements FullChunk,
ChunkManager {
    protected Map<Long, Entity> entities;
    protected Map<Long, BlockEntity> tiles;
    protected Map<Integer, BlockEntity> tileList;
    protected byte[] biomes;
    protected byte[] blocks;
    protected byte[] data;
    protected byte[] skyLight;
    protected byte[] blockLight;
    protected byte[] heightMap;
    protected List<CompoundTag> NBTtiles;
    protected List<CompoundTag> NBTentities;
    protected Map<Integer, Integer> extraData;
    protected LevelProvider provider;
    protected Class<? extends LevelProvider> providerClass;
    private int b;
    private int a;
    private long c;
    protected long changes;
    protected boolean isInit;
    protected Map<Integer, BatchPacket> chunkPackets;

    public BaseFullChunk clone() {
        BaseFullChunk baseFullChunk;
        block7: {
            try {
                baseFullChunk = (BaseFullChunk)super.clone();
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                return null;
            }
            if (this.biomes != null) {
                baseFullChunk.biomes = (byte[])this.biomes.clone();
            }
            if (this.blocks != null) {
                baseFullChunk.blocks = (byte[])this.blocks.clone();
            }
            if (this.data != null) {
                baseFullChunk.data = (byte[])this.data.clone();
            }
            if (this.skyLight != null) {
                baseFullChunk.skyLight = (byte[])this.skyLight.clone();
            }
            if (this.blockLight != null) {
                baseFullChunk.blockLight = (byte[])this.blockLight.clone();
            }
            if (this.heightMap == null) break block7;
            baseFullChunk.heightMap = (byte[])this.getHeightMapArray().clone();
        }
        return baseFullChunk;
    }

    protected BaseFullChunk fullClone() {
        BaseFullChunk baseFullChunk;
        try {
            baseFullChunk = (BaseFullChunk)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
        if (this.biomes != null) {
            baseFullChunk.biomes = (byte[])this.biomes.clone();
        }
        if (this.blocks != null) {
            baseFullChunk.blocks = (byte[])this.blocks.clone();
        }
        if (this.heightMap != null) {
            baseFullChunk.heightMap = (byte[])this.heightMap.clone();
        }
        if (this.entities != null) {
            baseFullChunk.entities = new Long2ObjectOpenHashMap<Entity>(this.entities);
        }
        if (this.tiles != null) {
            baseFullChunk.tiles = new Long2ObjectOpenHashMap<BlockEntity>(this.tiles);
        }
        baseFullChunk.tileList = null;
        baseFullChunk.NBTentities = null;
        baseFullChunk.NBTtiles = null;
        return baseFullChunk;
    }

    public void setChunkPacket(int n, BatchPacket batchPacket) {
        if (batchPacket != null) {
            batchPacket.trim();
            if (this.chunkPackets == null) {
                this.chunkPackets = new Int2ObjectOpenHashMap<BatchPacket>();
            }
            this.chunkPackets.put(n, batchPacket);
        }
    }

    public BatchPacket getChunkPacket(int n) {
        BatchPacket batchPacket;
        block1: {
            if (this.chunkPackets == null) {
                return null;
            }
            batchPacket = this.chunkPackets.get(n);
            if (batchPacket == null) break block1;
            batchPacket.trim();
        }
        return batchPacket;
    }

    @Override
    public void initChunk() {
        if (this.getProvider() != null && !this.isInit) {
            Object object;
            Timing timing;
            boolean bl = false;
            if (this.NBTentities != null) {
                timing = this.getProvider().getLevel().timings.syncChunkLoadEntitiesTimer;
                if (timing != null) {
                    timing.startTiming();
                }
                for (CompoundTag compoundTag : this.NBTentities) {
                    if (!compoundTag.contains("id")) {
                        this.setChanged();
                        continue;
                    }
                    object = compoundTag.getList("Pos");
                    if (((Number)((NumberTag)((ListTag)object).get(0)).getData()).intValue() >> 4 != this.b || ((Number)((NumberTag)((ListTag)object).get(2)).getData()).intValue() >> 4 != this.a) {
                        bl = true;
                        continue;
                    }
                    Entity entity = Entity.createEntity(compoundTag.getString("id"), (FullChunk)this, compoundTag, new Object[0]);
                    if (entity == null) continue;
                    bl = true;
                }
                if (timing != null) {
                    timing.stopTiming();
                }
                this.NBTentities = null;
            }
            if (this.NBTtiles != null) {
                timing = this.getProvider().getLevel().timings.syncChunkLoadBlockEntitiesTimer;
                if (timing != null) {
                    timing.startTiming();
                }
                for (CompoundTag compoundTag : this.NBTtiles) {
                    if (compoundTag == null) continue;
                    if (!compoundTag.contains("id")) {
                        bl = true;
                        continue;
                    }
                    if (compoundTag.getInt("x") >> 4 != this.b || compoundTag.getInt("z") >> 4 != this.a) {
                        bl = true;
                        continue;
                    }
                    object = BlockEntity.createBlockEntity(compoundTag.getString("id").replaceFirst("BlockEntity", ""), this, compoundTag, new Object[0]);
                    if (object != null) continue;
                    bl = true;
                }
                if (timing != null) {
                    timing.stopTiming();
                }
                this.NBTtiles = null;
            }
            this.setChanged(bl);
            this.isInit = true;
        }
    }

    @Override
    public final long getIndex() {
        return this.c;
    }

    @Override
    public final int getX() {
        return this.b;
    }

    @Override
    public final int getZ() {
        return this.a;
    }

    @Override
    public void setPosition(int n, int n2) {
        this.b = n;
        this.a = n2;
        this.c = Level.chunkHash(n, n2);
    }

    @Override
    public final void setX(int n) {
        this.b = n;
        this.c = Level.chunkHash(n, this.a);
    }

    @Override
    public final void setZ(int n) {
        this.a = n;
        this.c = Level.chunkHash(this.b, n);
    }

    @Override
    public LevelProvider getProvider() {
        return this.provider;
    }

    @Override
    public void setProvider(LevelProvider levelProvider) {
        block0: {
            this.provider = levelProvider;
            if (levelProvider == null) break block0;
            this.providerClass = levelProvider.getClass();
        }
    }

    @Override
    public int getBiomeId(int n, int n2) {
        return this.biomes[n << 4 | n2] & 0xFF;
    }

    @Override
    public void setBiomeId(int n, int n2, byte by) {
        this.biomes[n << 4 | n2] = by;
    }

    @Override
    public void setBiomeId(int n, int n2, int n3) {
        this.biomes[n << 4 | n2] = (byte)n3;
    }

    @Override
    public int getBiomeColor(int n, int n2) {
        return 0;
    }

    @Override
    public void setBiomeIdAndColor(int n, int n2, int n3) {
    }

    @Override
    public void setBiomeColor(int n, int n2, int n3, int n4, int n5) {
    }

    @Override
    public int getHeightMap(int n, int n2) {
        return this.heightMap[n2 << 4 | n] & 0xFF;
    }

    @Override
    public void setHeightMap(int n, int n2, int n3) {
        this.heightMap[n2 << 4 | n] = (byte)n3;
    }

    @Override
    public void recalculateHeightMap() {
        for (int k = 0; k < 16; ++k) {
            for (int i2 = 0; i2 < 16; ++i2) {
                this.setHeightMap(i2, k, this.getHighestBlockAt(i2, k, false));
            }
        }
    }

    @Override
    public int getBlockExtraData(int n, int n2, int n3) {
        int n4 = Level.chunkBlockHash(n, n2, n3);
        if (this.extraData != null && this.extraData.containsKey(n4)) {
            return this.extraData.get(n4);
        }
        return 0;
    }

    @Override
    public void setBlockExtraData(int n, int n2, int n3, int n4) {
        if (n4 == 0) {
            if (this.extraData != null) {
                this.extraData.remove(Level.chunkBlockHash(n, n2, n3));
            }
        } else {
            if (this.extraData == null) {
                this.extraData = new Int2ObjectOpenHashMap<Integer>();
            }
            this.extraData.put(Level.chunkBlockHash(n, n2, n3), n4);
        }
        this.setChanged(true);
    }

    @Override
    public void populateSkyLight() {
    }

    @Override
    public int getHighestBlockAt(int n, int n2) {
        return this.getHighestBlockAt(n, n2, true);
    }

    @Override
    public int getHighestBlockAt(int n, int n2, boolean bl) {
        int n3;
        if (bl && (n3 = this.getHeightMap(n, n2)) != 0 && n3 != 255) {
            return n3;
        }
        for (n3 = 255; n3 >= 0; --n3) {
            if (this.getBlockId(n, n3, n2) == 0) continue;
            if (bl) {
                this.setHeightMap(n, n2, n3);
            }
            return n3;
        }
        return 0;
    }

    @Override
    public void addEntity(Entity entity) {
        block2: {
            if (this.entities == null) {
                this.entities = new Long2ObjectOpenHashMap<Entity>();
            }
            if (entity.getServer().suomiCraftPEMode() && !entity.goToNewChunk(this)) {
                return;
            }
            this.entities.put(entity.getId(), entity);
            if (entity instanceof Player || !this.isInit) break block2;
            this.setChanged();
        }
    }

    @Override
    public void removeEntity(Entity entity) {
        block1: {
            if (this.entities == null) break block1;
            this.entities.remove(entity.getId());
            if (!(entity instanceof Player) && this.isInit) {
                this.setChanged();
            }
        }
    }

    @Override
    public void addBlockEntity(BlockEntity blockEntity) {
        block2: {
            if (this.tiles == null) {
                this.tiles = new Long2ObjectOpenHashMap<BlockEntity>();
                this.tileList = new Int2ObjectOpenHashMap<BlockEntity>();
            }
            this.tiles.put(blockEntity.getId(), blockEntity);
            int n = (blockEntity.getFloorZ() & 0xF) << 12 | (blockEntity.getFloorX() & 0xF) << 8 | blockEntity.getFloorY() & 0xFF;
            if (this.tileList.containsKey(n) && !this.tileList.get(n).equals(blockEntity)) {
                BlockEntity blockEntity2 = this.tileList.get(n);
                this.tiles.remove(blockEntity2.getId());
                blockEntity2.close();
            }
            this.tileList.put(n, blockEntity);
            if (!this.isInit) break block2;
            this.setChanged();
        }
    }

    @Override
    public void removeBlockEntity(BlockEntity blockEntity) {
        block1: {
            if (this.tiles == null) break block1;
            this.tiles.remove(blockEntity.getId());
            this.tileList.remove((blockEntity.getFloorZ() & 0xF) << 12 | (blockEntity.getFloorX() & 0xF) << 8 | blockEntity.getFloorY() & 0xFF);
            if (this.isInit) {
                this.setChanged();
            }
        }
    }

    @Override
    public Map<Long, Entity> getEntities() {
        return this.entities == null ? Collections.emptyMap() : this.entities;
    }

    @Override
    public Map<Long, BlockEntity> getBlockEntities() {
        return this.tiles == null ? Collections.emptyMap() : this.tiles;
    }

    @Override
    public Map<Integer, Integer> getBlockExtraDataArray() {
        return this.extraData == null ? Collections.emptyMap() : this.extraData;
    }

    @Override
    public BlockEntity getTile(int n, int n2, int n3) {
        return this.tileList != null ? this.tileList.get(n3 << 12 | n << 8 | n2) : null;
    }

    @Override
    public boolean isLoaded() {
        return this.getProvider() != null && this.getProvider().isChunkLoaded(this.b, this.a);
    }

    @Override
    public boolean load() throws IOException {
        return this.load(true);
    }

    @Override
    public boolean load(boolean bl) throws IOException {
        return this.getProvider() != null && this.getProvider().getChunk(this.b, this.a, true) != null;
    }

    @Override
    public boolean unload() throws Exception {
        return this.unload(this.provider.getLevel().getAutoSave(), true);
    }

    @Override
    public boolean unload(boolean bl) throws Exception {
        return this.unload(bl, true);
    }

    @Override
    public boolean unload(boolean bl, boolean bl2) {
        LevelProvider levelProvider = this.getProvider();
        if (levelProvider == null) {
            return true;
        }
        if (bl && this.changes != 0L) {
            levelProvider.saveChunk(this.b, this.a);
        }
        if (bl2) {
            for (Entity position : this.getEntities().values()) {
                if (!(position instanceof Player)) continue;
                return false;
            }
        }
        for (Entity entity : new ArrayList<Entity>(this.getEntities().values())) {
            if (entity instanceof Player) continue;
            entity.close();
        }
        for (BlockEntity blockEntity : new ArrayList<BlockEntity>(this.getBlockEntities().values())) {
            blockEntity.close();
        }
        this.provider = null;
        return true;
    }

    @Override
    public byte[] getBlockIdArray() {
        return this.blocks;
    }

    @Override
    public byte[] getBlockDataArray() {
        return this.data;
    }

    @Override
    public byte[] getBlockSkyLightArray() {
        return this.skyLight;
    }

    @Override
    public byte[] getBlockLightArray() {
        return this.blockLight;
    }

    @Override
    public byte[] getBiomeIdArray() {
        return this.biomes;
    }

    @Override
    public int[] getBiomeColorArray() {
        return new int[0];
    }

    @Override
    public byte[] getHeightMapArray() {
        return this.heightMap;
    }

    public long getChanges() {
        return this.changes;
    }

    @Override
    public boolean hasChanged() {
        return this.changes != 0L;
    }

    @Override
    public void setChanged() {
        ++this.changes;
        this.chunkPackets = null;
    }

    @Override
    public void setChanged(boolean bl) {
        if (bl) {
            this.setChanged();
        } else {
            this.changes = 0L;
        }
    }

    @Override
    public byte[] toFastBinary() {
        return this.toBinary();
    }

    @Override
    public boolean isLightPopulated() {
        return true;
    }

    @Override
    public void setLightPopulated() {
        this.setLightPopulated(true);
    }

    @Override
    public void setLightPopulated(boolean bl) {
    }

    @Override
    public int getBlockIdAt(int n, int n2, int n3) {
        if (n >> 4 == this.b && n3 >> 4 == this.a) {
            return this.getBlockId(n & 0xF, n2, n3 & 0xF);
        }
        return 0;
    }

    @Override
    public void setBlockFullIdAt(int n, int n2, int n3, int n4) {
        block0: {
            if (n >> 4 != this.b || n3 >> 4 != this.a) break block0;
            this.setFullBlockId(n & 0xF, n2, n3 & 0xF, n4);
        }
    }

    @Override
    public void setBlockIdAt(int n, int n2, int n3, int n4) {
        block0: {
            if (n >> 4 != this.b || n3 >> 4 != this.a) break block0;
            this.setBlockId(n & 0xF, n2, n3 & 0xF, n4);
        }
    }

    @Override
    public void setBlockAt(int n, int n2, int n3, int n4, int n5) {
        block0: {
            if (n >> 4 != this.b || n3 >> 4 != this.a) break block0;
            this.setBlock(n & 0xF, n2, n3 & 0xF, n4, n5);
        }
    }

    @Override
    public int getBlockDataAt(int n, int n2, int n3) {
        if (n >> 4 == this.b && n3 >> 4 == this.a) {
            return this.getBlockIdAt(n & 0xF, n2, n3 & 0xF);
        }
        return 0;
    }

    @Override
    public void setBlockDataAt(int n, int n2, int n3, int n4) {
        block0: {
            if (n >> 4 != this.b || n3 >> 4 != this.a) break block0;
            this.setBlockData(n & 0xF, n2, n3 & 0xF, n4);
        }
    }

    @Override
    public BaseFullChunk getChunk(int n, int n2) {
        if (n == this.b && n2 == this.a) {
            return this;
        }
        return null;
    }

    @Override
    public void setChunk(int n, int n2) {
        this.setChunk(n, n2, null);
    }

    @Override
    public void setChunk(int n, int n2, BaseFullChunk baseFullChunk) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getSeed() {
        throw new UnsupportedOperationException("Chunk does not have a seed");
    }

    public boolean compress() {
        if (this.chunkPackets == null) {
            return false;
        }
        for (BatchPacket batchPacket : this.chunkPackets.values()) {
            if (batchPacket == null) continue;
            batchPacket.trim();
        }
        return !this.chunkPackets.isEmpty();
    }

    private static Exception b(Exception exception) {
        return exception;
    }
}

