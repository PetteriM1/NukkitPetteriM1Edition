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

    void setCurrentTick(long currentTick);

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

    void setRainTime(int rainTime);

    long getSeed();

    void setSeed(long value);

    Vector3 getSpawn();

    void setSpawn(Vector3 pos);

    int getThunderTime();

    void setThunderTime(int thunderTime);

    long getTime();

    void setTime(long value);

    boolean isChunkGenerated(int X, int Z);

    boolean isChunkLoaded(int X, int Z);

    boolean isChunkLoaded(long hash);

    boolean isChunkPopulated(int X, int Z);

    boolean isRaining();

    void setRaining(boolean raining);

    boolean isThundering();

    void setThundering(boolean thundering);

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

    void setGameRules(GameRules rules);

    boolean unloadChunk(int X, int Z);

    boolean unloadChunk(int X, int Z, boolean safe);

    void unloadChunks();

    void updateLevelName(String name);
}
