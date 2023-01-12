/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.generic;

import cn.nukkit.Server;
import cn.nukkit.level.GameRules;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.format.generic.BaseRegionLoader;
import cn.nukkit.level.format.generic.a;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.ChunkException;
import cn.nukkit.utils.LevelException;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public abstract class BaseLevelProvider
implements LevelProvider {
    protected Level level;
    protected final String path;
    protected CompoundTag levelData;
    private Vector3 a;
    protected final AtomicReference<BaseRegionLoader> lastRegion = new AtomicReference();
    protected final Long2ObjectMap<BaseRegionLoader> regions = new Long2ObjectOpenHashMap<BaseRegionLoader>();
    protected final Long2ObjectMap<BaseFullChunk> chunks = new Long2ObjectOpenHashMap<BaseFullChunk>();
    private final AtomicReference<BaseFullChunk> b = new AtomicReference();

    public BaseLevelProvider(Level level, String string) throws IOException {
        CompoundTag compoundTag;
        this.level = level;
        this.path = string;
        File file = new File(this.path);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!((compoundTag = NBTIO.readCompressed(new FileInputStream(new File(this.path + "level.dat")), ByteOrder.BIG_ENDIAN)).get("Data") instanceof CompoundTag)) {
            throw new LevelException("Invalid level.dat");
        }
        this.levelData = compoundTag.getCompound("Data");
        if (!this.levelData.contains("generatorName")) {
            this.levelData.putString("generatorName", Generator.getGenerator("DEFAULT").getSimpleName().toLowerCase());
        }
        if (!this.levelData.contains("generatorOptions")) {
            this.levelData.putString("generatorOptions", "");
        }
        this.a = new Vector3(this.levelData.getInt("SpawnX"), this.levelData.getInt("SpawnY"), this.levelData.getInt("SpawnZ"));
    }

    public abstract BaseFullChunk loadChunk(long var1, int var3, int var4, boolean var5);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int size() {
        Long2ObjectMap<BaseFullChunk> long2ObjectMap = this.chunks;
        synchronized (long2ObjectMap) {
            return this.chunks.size();
        }
    }

    @Override
    public void unloadChunks() {
        Iterator iterator = this.chunks.values().iterator();
        while (iterator.hasNext()) {
            ((BaseFullChunk)iterator.next()).unload(this.level.getAutoSave(), false);
            iterator.remove();
        }
    }

    @Override
    public String getGenerator() {
        return this.levelData.getString("generatorName");
    }

    @Override
    public Map<String, Object> getGeneratorOptions() {
        return new a(this);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Map<Long, BaseFullChunk> getLoadedChunks() {
        Long2ObjectMap<BaseFullChunk> long2ObjectMap = this.chunks;
        synchronized (long2ObjectMap) {
            return ImmutableMap.copyOf(this.chunks);
        }
    }

    @Override
    public boolean isChunkLoaded(int n, int n2) {
        return this.isChunkLoaded(Level.chunkHash(n, n2));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void putChunk(long l, BaseFullChunk baseFullChunk) {
        Long2ObjectMap<BaseFullChunk> long2ObjectMap = this.chunks;
        synchronized (long2ObjectMap) {
            this.chunks.put(l, baseFullChunk);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isChunkLoaded(long l) {
        Long2ObjectMap<BaseFullChunk> long2ObjectMap = this.chunks;
        synchronized (long2ObjectMap) {
            return this.chunks.containsKey(l);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public BaseRegionLoader getRegion(int n, int n2) {
        long l = Level.chunkHash(n, n2);
        Long2ObjectMap<BaseRegionLoader> long2ObjectMap = this.regions;
        synchronized (long2ObjectMap) {
            return (BaseRegionLoader)this.regions.get(l);
        }
    }

    protected static int getRegionIndexX(int n) {
        return n >> 5;
    }

    protected static int getRegionIndexZ(int n) {
        return n >> 5;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    public Server getServer() {
        return this.level.getServer();
    }

    @Override
    public Level getLevel() {
        return this.level;
    }

    @Override
    public String getName() {
        return this.levelData.getString("LevelName");
    }

    @Override
    public boolean isRaining() {
        return this.levelData.getBoolean("raining");
    }

    @Override
    public void setRaining(boolean bl) {
        this.levelData.putBoolean("raining", bl);
    }

    @Override
    public int getRainTime() {
        return this.levelData.getInt("rainTime");
    }

    @Override
    public void setRainTime(int n) {
        this.levelData.putInt("rainTime", n);
    }

    @Override
    public boolean isThundering() {
        return this.levelData.getBoolean("thundering");
    }

    @Override
    public void setThundering(boolean bl) {
        this.levelData.putBoolean("thundering", bl);
    }

    @Override
    public int getThunderTime() {
        return this.levelData.getInt("thunderTime");
    }

    @Override
    public void setThunderTime(int n) {
        this.levelData.putInt("thunderTime", n);
    }

    @Override
    public long getCurrentTick() {
        return this.levelData.getLong("Time");
    }

    @Override
    public void setCurrentTick(long l) {
        this.levelData.putLong("Time", l);
    }

    @Override
    public long getTime() {
        return this.levelData.getLong("DayTime");
    }

    @Override
    public void setTime(long l) {
        this.levelData.putLong("DayTime", l);
    }

    @Override
    public long getSeed() {
        return this.levelData.getLong("RandomSeed");
    }

    @Override
    public void setSeed(long l) {
        this.levelData.putLong("RandomSeed", l);
    }

    @Override
    public Vector3 getSpawn() {
        return this.a;
    }

    @Override
    public void setSpawn(Vector3 vector3) {
        this.levelData.putInt("SpawnX", (int)vector3.x);
        this.levelData.putInt("SpawnY", (int)vector3.y);
        this.levelData.putInt("SpawnZ", (int)vector3.z);
        this.a = vector3;
    }

    @Override
    public GameRules getGamerules() {
        GameRules gameRules;
        block0: {
            gameRules = GameRules.getDefault();
            if (!this.levelData.contains("GameRules")) break block0;
            gameRules.readNBT(this.levelData.getCompound("GameRules"));
        }
        return gameRules;
    }

    @Override
    public void setGameRules(GameRules gameRules) {
        this.levelData.putCompound("GameRules", gameRules.writeNBT());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void doGarbageCollection() {
        int n = (int)(System.currentTimeMillis() - 50L);
        Long2ObjectMap<BaseRegionLoader> long2ObjectMap = this.regions;
        synchronized (long2ObjectMap) {
            if (this.regions.isEmpty()) {
                return;
            }
            Iterator iterator = this.regions.values().iterator();
            while (iterator.hasNext()) {
                BaseRegionLoader baseRegionLoader = (BaseRegionLoader)iterator.next();
                if (baseRegionLoader.lastUsed > (long)n) continue;
                try {
                    baseRegionLoader.close();
                }
                catch (IOException iOException) {
                    throw new RuntimeException("Unable to close RegionLoader", iOException);
                }
                this.lastRegion.set(null);
                iterator.remove();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void saveChunks() {
        Long2ObjectMap<BaseFullChunk> long2ObjectMap = this.chunks;
        synchronized (long2ObjectMap) {
            for (BaseFullChunk baseFullChunk : this.chunks.values()) {
                if (baseFullChunk.getChanges() == 0L) continue;
                baseFullChunk.setChanged(false);
                this.saveChunk(baseFullChunk.getX(), baseFullChunk.getZ());
            }
        }
    }

    public CompoundTag getLevelData() {
        return this.levelData;
    }

    @Override
    public void saveLevelData() {
        String string = this.path + "level.dat";
        File file = new File(string);
        if (file.exists()) {
            try {
                Files.copy(file, new File(string + ".bak"));
            }
            catch (IOException iOException) {
                Server.getInstance().getLogger().logException(iOException);
            }
        }
        try {
            NBTIO.writeGZIPCompressed(new CompoundTag().putCompound("Data", this.levelData), new FileOutputStream(string));
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    @Override
    public void updateLevelName(String string) {
        block0: {
            if (this.getName().equals(string)) break block0;
            this.levelData.putString("LevelName", string);
        }
    }

    @Override
    public boolean loadChunk(int n, int n2) {
        return this.loadChunk(n, n2, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean loadChunk(int n, int n2, boolean bl) {
        long l = Level.chunkHash(n, n2);
        Long2ObjectMap<BaseFullChunk> long2ObjectMap = this.chunks;
        synchronized (long2ObjectMap) {
            if (this.chunks.containsKey(l)) {
                return true;
            }
        }
        return this.loadChunk(l, n, n2, bl) != null;
    }

    @Override
    public boolean unloadChunk(int n, int n2) {
        return this.unloadChunk(n, n2, true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean unloadChunk(int n, int n2, boolean bl) {
        long l = Level.chunkHash(n, n2);
        Long2ObjectMap<BaseFullChunk> long2ObjectMap = this.chunks;
        synchronized (long2ObjectMap) {
            BaseFullChunk baseFullChunk = (BaseFullChunk)this.chunks.get(l);
            if (baseFullChunk != null && baseFullChunk.unload(false, bl)) {
                this.b.set(null);
                this.chunks.remove(l, (Object)baseFullChunk);
                return true;
            }
        }
        return false;
    }

    @Override
    public BaseFullChunk getChunk(int n, int n2) {
        return this.getChunk(n, n2, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public BaseFullChunk getLoadedChunk(int n, int n2) {
        BaseFullChunk baseFullChunk = this.b.get();
        if (baseFullChunk != null && baseFullChunk.getX() == n && baseFullChunk.getZ() == n2) {
            return baseFullChunk;
        }
        long l = Level.chunkHash(n, n2);
        Long2ObjectMap<BaseFullChunk> long2ObjectMap = this.chunks;
        synchronized (long2ObjectMap) {
            baseFullChunk = (BaseFullChunk)this.chunks.get(l);
            this.b.set(baseFullChunk);
        }
        return baseFullChunk;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public BaseFullChunk getLoadedChunk(long l) {
        BaseFullChunk baseFullChunk = this.b.get();
        if (baseFullChunk != null && baseFullChunk.getIndex() == l) {
            return baseFullChunk;
        }
        Long2ObjectMap<BaseFullChunk> long2ObjectMap = this.chunks;
        synchronized (long2ObjectMap) {
            baseFullChunk = (BaseFullChunk)this.chunks.get(l);
            this.b.set(baseFullChunk);
        }
        return baseFullChunk;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public BaseFullChunk getChunk(int n, int n2, boolean bl) {
        BaseFullChunk baseFullChunk = this.b.get();
        if (baseFullChunk != null && baseFullChunk.getX() == n && baseFullChunk.getZ() == n2) {
            return baseFullChunk;
        }
        long l = Level.chunkHash(n, n2);
        Long2ObjectMap<BaseFullChunk> long2ObjectMap = this.chunks;
        synchronized (long2ObjectMap) {
            baseFullChunk = (BaseFullChunk)this.chunks.get(l);
            this.b.set(baseFullChunk);
        }
        if (baseFullChunk == null) {
            baseFullChunk = this.loadChunk(l, n, n2, bl);
            this.b.set(baseFullChunk);
        }
        return baseFullChunk;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setChunk(int n, int n2, FullChunk fullChunk) {
        if (!(fullChunk instanceof BaseFullChunk)) {
            throw new ChunkException("Invalid chunk class (" + n + ", " + n2 + ')');
        }
        fullChunk.setProvider(this);
        fullChunk.setPosition(n, n2);
        long l = Level.chunkHash(n, n2);
        Long2ObjectMap<BaseFullChunk> long2ObjectMap = this.chunks;
        synchronized (long2ObjectMap) {
            if (this.chunks.containsKey(l) && !((BaseFullChunk)this.chunks.get(l)).equals(fullChunk)) {
                this.unloadChunk(n, n2, false);
            }
            this.chunks.put(l, (BaseFullChunk)fullChunk);
        }
    }

    @Override
    public boolean isChunkPopulated(int n, int n2) {
        BaseFullChunk baseFullChunk = this.getChunk(n, n2);
        return baseFullChunk != null && baseFullChunk.isPopulated();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public synchronized void close() {
        this.unloadChunks();
        Long2ObjectMap<BaseRegionLoader> long2ObjectMap = this.regions;
        synchronized (long2ObjectMap) {
            Iterator iterator = this.regions.values().iterator();
            while (iterator.hasNext()) {
                try {
                    ((BaseRegionLoader)iterator.next()).close();
                }
                catch (IOException iOException) {
                    throw new RuntimeException("Unable to close RegionLoader", iOException);
                }
                this.lastRegion.set(null);
                iterator.remove();
            }
        }
        this.level = null;
    }

    @Override
    public boolean isChunkGenerated(int n, int n2) {
        BaseRegionLoader baseRegionLoader = this.getRegion(n >> 5, n2 >> 5);
        return baseRegionLoader != null && baseRegionLoader.chunkExists(n - (baseRegionLoader.getX() << 5), n2 - (baseRegionLoader.getZ() << 5)) && this.getChunk(n - (baseRegionLoader.getX() << 5), n2 - (baseRegionLoader.getZ() << 5), true).isGenerated();
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

