package cn.nukkit.level.format;

import cn.nukkit.level.GameRules;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.Vector3;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public interface LevelProvider {

    void close();

    void doGarbageCollection();

    default void doGarbageCollection(long time) {

    }

    BaseFullChunk getChunk(int X, int Z);

    BaseFullChunk getChunk(int X, int Z, boolean create);

    long getCurrentTick();

    BaseFullChunk getEmptyChunk(int x, int z);

    GameRules getGamerules();

    String getGenerator();

    Map<String, Object> getGeneratorOptions();

    Level getLevel();

    BaseFullChunk getLoadedChunk(int X, int Z);

    BaseFullChunk getLoadedChunk(long hash);

    Map<Long, ? extends FullChunk> getLoadedChunks();

    String getName();

    String getPath();

    int getRainTime();

    long getSeed();

    Vector3 getSpawn();

    int getThunderTime();

    long getTime();

    boolean isChunkGenerated(int X, int Z);

    boolean isChunkLoaded(int X, int Z);

    boolean isChunkLoaded(long hash);

    boolean isChunkPopulated(int X, int Z);

    boolean isRaining();

    boolean isThundering();

    boolean loadChunk(int X, int Z);

    boolean loadChunk(int X, int Z, boolean create);

    void requestChunkTask(IntSet protocols, int X, int Z);

    void saveChunk(int X, int Z);

    void saveChunk(int X, int Z, FullChunk chunk);

    void saveChunks();

    default CompletableFuture<Void> saveChunksFuture() {
        this.saveChunks();
        return CompletableFuture.completedFuture(null);
    }

    void saveLevelData();

    void setChunk(int chunkX, int chunkZ, FullChunk chunk);

    void setCurrentTick(long currentTick);

    void setGameRules(GameRules rules);

    void setRainTime(int rainTime);

    void setRaining(boolean raining);

    void setSeed(long value);

    void setSpawn(Vector3 pos);

    void setThunderTime(int thunderTime);

    void setThundering(boolean thundering);

    void setTime(long value);

    boolean unloadChunk(int X, int Z);

    boolean unloadChunk(int X, int Z, boolean safe);

    void unloadChunks();

    void updateLevelName(String name);
}
