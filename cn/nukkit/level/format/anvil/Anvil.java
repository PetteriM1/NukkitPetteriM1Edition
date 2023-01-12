/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil;

import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.anvil.Chunk;
import cn.nukkit.level.format.anvil.ChunkSection;
import cn.nukkit.level.format.anvil.RegionLoader;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.format.generic.BaseLevelProvider;
import cn.nukkit.level.format.generic.BaseRegionLoader;
import cn.nukkit.level.format.generic.serializer.NetworkChunkData;
import cn.nukkit.level.format.generic.serializer.NetworkChunkSerializer;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.ChunkException;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

public class Anvil
extends BaseLevelProvider {
    public static final byte[] PAD_256 = new byte[256];
    private int c = 0;

    public Anvil(Level level, String string) throws IOException {
        super(level, string);
    }

    public static String getProviderName() {
        return "anvil";
    }

    public static byte getProviderOrder() {
        return 0;
    }

    public static boolean usesChunkSection() {
        return true;
    }

    public static boolean isValid(String string2) {
        boolean bl;
        boolean bl2 = bl = new File(string2 + "/level.dat").exists() && new File(string2 + "/region/").isDirectory();
        if (bl) {
            for (File file2 : new File(string2 + "/region/").listFiles((file, string) -> Pattern.matches("^.+\\.mc[r|a]$", string))) {
                if (file2.getName().endsWith(".mca")) continue;
                bl = false;
                break;
            }
        }
        return bl;
    }

    public static void generate(String string, String string2, long l, Class<? extends Generator> clazz) throws IOException {
        Anvil.generate(string, string2, l, clazz, new HashMap<String, String>());
    }

    public static void generate(String string, String string2, long l, Class<? extends Generator> clazz, Map<String, String> map) throws IOException {
        if (!new File(string + "/region").exists()) {
            new File(string + "/region").mkdirs();
        }
        CompoundTag compoundTag = new CompoundTag("Data").putCompound("GameRules", new CompoundTag()).putLong("DayTime", 0L).putInt("GameType", 0).putString("generatorName", Generator.getGeneratorName(clazz)).putString("generatorOptions", map.getOrDefault("preset", "")).putInt("generatorVersion", 1).putBoolean("hardcore", false).putBoolean("initialized", true).putLong("LastPlayed", System.currentTimeMillis() / 1000L).putString("LevelName", string2).putBoolean("raining", false).putInt("rainTime", 0).putLong("RandomSeed", l).putInt("SpawnX", 128).putInt("SpawnY", 70).putInt("SpawnZ", 128).putBoolean("thundering", false).putInt("thunderTime", 0).putInt("version", 19133).putLong("Time", 0L).putLong("SizeOnDisk", 0L);
        NBTIO.writeGZIPCompressed(new CompoundTag().putCompound("Data", compoundTag), new FileOutputStream(string + "level.dat"), ByteOrder.BIG_ENDIAN);
    }

    @Override
    public Chunk getEmptyChunk(int n, int n2) {
        return Chunk.getEmptyChunk(n, n2, this);
    }

    @Override
    public void requestChunkTask(IntSet intSet, int n, int n2) throws ChunkException {
        Chunk chunk = (Chunk)this.getChunk(n, n2, false);
        if (chunk == null) {
            throw new ChunkException("Invalid chunk set (" + n + ", " + n2 + ')');
        }
        long l = chunk.getChanges();
        if (this.getServer().asyncChunkSending) {
            Chunk chunk2 = chunk.fullClone();
            this.level.asyncChunk(intSet, chunk2, l, n, n2);
            return;
        }
        BiConsumer<BinaryStream, NetworkChunkData> biConsumer = (binaryStream, networkChunkData) -> this.getLevel().chunkRequestCallback(networkChunkData.getProtocol(), l, n, n2, networkChunkData.getChunkSections(), binaryStream.getBuffer());
        NetworkChunkSerializer.serialize(chunk, intSet, biConsumer, this.level.antiXrayEnabled(), this.level.getDimensionData());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void doGarbageCollection(long l) {
        int n;
        long l2 = System.currentTimeMillis();
        int n2 = this.size();
        if (this.c > n2) {
            this.c = 0;
        }
        Long2ObjectMap long2ObjectMap = this.chunks;
        synchronized (long2ObjectMap) {
            Iterator iterator = this.chunks.values().iterator();
            if (this.c != 0) {
                iterator.skip(this.c);
            }
            for (n = 0; n < n2; ++n) {
                if (!iterator.hasNext()) {
                    iterator = this.chunks.values().iterator();
                }
                if (!iterator.hasNext()) break;
                BaseFullChunk baseFullChunk = (BaseFullChunk)iterator.next();
                if (baseFullChunk == null || !baseFullChunk.isGenerated() || !baseFullChunk.isPopulated() || !(baseFullChunk instanceof Chunk)) continue;
                baseFullChunk.compress();
                if (System.currentTimeMillis() - l2 >= l) break;
            }
        }
        this.c += n;
    }

    @Override
    public synchronized BaseFullChunk loadChunk(long l, int n, int n2, boolean bl) {
        BaseFullChunk baseFullChunk;
        block6: {
            int n3 = Anvil.getRegionIndexX(n);
            int n4 = Anvil.getRegionIndexZ(n2);
            BaseRegionLoader baseRegionLoader = this.loadRegion(n3, n4);
            if (this.level.timings.syncChunkLoadDataTimer != null) {
                this.level.timings.syncChunkLoadDataTimer.startTiming();
            }
            try {
                baseFullChunk = baseRegionLoader.readChunk(n - (n3 << 5), n2 - (n4 << 5));
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
            if (baseFullChunk == null) {
                if (bl) {
                    baseFullChunk = this.getEmptyChunk(n, n2);
                    this.putChunk(l, baseFullChunk);
                }
            } else {
                this.putChunk(l, baseFullChunk);
            }
            if (this.level.timings.syncChunkLoadDataTimer == null) break block6;
            this.level.timings.syncChunkLoadDataTimer.stopTiming();
        }
        return baseFullChunk;
    }

    @Override
    public synchronized void saveChunk(int n, int n2) {
        BaseFullChunk baseFullChunk = this.getChunk(n, n2);
        if (baseFullChunk != null) {
            try {
                this.loadRegion(n >> 5, n2 >> 5).writeChunk(baseFullChunk);
            }
            catch (Exception exception) {
                throw new ChunkException("Error saving chunk (" + n + ", " + n2 + ')', exception);
            }
        }
    }

    @Override
    public synchronized void saveChunk(int n, int n2, FullChunk fullChunk) {
        if (!(fullChunk instanceof Chunk)) {
            throw new ChunkException("Invalid chunk class (" + n + ", " + n2 + ')');
        }
        int n3 = n >> 5;
        int n4 = n2 >> 5;
        this.loadRegion(n3, n4);
        fullChunk.setX(n);
        fullChunk.setZ(n2);
        try {
            this.getRegion(n3, n4).writeChunk(fullChunk);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static ChunkSection createChunkSection(int n) {
        ChunkSection chunkSection = new ChunkSection(n);
        chunkSection.hasSkyLight = true;
        return chunkSection;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected synchronized BaseRegionLoader loadRegion(int n, int n2) {
        BaseRegionLoader baseRegionLoader = (BaseRegionLoader)this.lastRegion.get();
        if (baseRegionLoader != null && n == baseRegionLoader.getX() && n2 == baseRegionLoader.getZ()) {
            return baseRegionLoader;
        }
        long l = Level.chunkHash(n, n2);
        Long2ObjectMap long2ObjectMap = this.regions;
        synchronized (long2ObjectMap) {
            BaseRegionLoader baseRegionLoader2 = (BaseRegionLoader)this.regions.get(l);
            if (baseRegionLoader2 == null) {
                try {
                    baseRegionLoader2 = new RegionLoader(this, n, n2);
                }
                catch (IOException iOException) {
                    throw new RuntimeException(iOException);
                }
                this.regions.put(l, baseRegionLoader2);
            }
            this.lastRegion.set(baseRegionLoader2);
            return baseRegionLoader2;
        }
    }

    private static Exception b(Exception exception) {
        return exception;
    }
}

