/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format;

import cn.nukkit.level.GameRules;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.Vector3;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Map;

public interface LevelProvider {
    public static final byte ORDER_YZX = 0;
    public static final byte ORDER_ZXY = 1;

    public void requestChunkTask(IntSet var1, int var2, int var3);

    public String getPath();

    public String getGenerator();

    public Map<String, Object> getGeneratorOptions();

    public BaseFullChunk getLoadedChunk(int var1, int var2);

    public BaseFullChunk getLoadedChunk(long var1);

    public BaseFullChunk getChunk(int var1, int var2);

    public BaseFullChunk getChunk(int var1, int var2, boolean var3);

    public BaseFullChunk getEmptyChunk(int var1, int var2);

    public void saveChunks();

    public void saveChunk(int var1, int var2);

    public void saveChunk(int var1, int var2, FullChunk var3);

    public void unloadChunks();

    public boolean loadChunk(int var1, int var2);

    public boolean loadChunk(int var1, int var2, boolean var3);

    public boolean unloadChunk(int var1, int var2);

    public boolean unloadChunk(int var1, int var2, boolean var3);

    public boolean isChunkGenerated(int var1, int var2);

    public boolean isChunkPopulated(int var1, int var2);

    public boolean isChunkLoaded(int var1, int var2);

    public boolean isChunkLoaded(long var1);

    public void setChunk(int var1, int var2, FullChunk var3);

    public String getName();

    public boolean isRaining();

    public void setRaining(boolean var1);

    public int getRainTime();

    public void setRainTime(int var1);

    public boolean isThundering();

    public void setThundering(boolean var1);

    public int getThunderTime();

    public void setThunderTime(int var1);

    public long getCurrentTick();

    public void setCurrentTick(long var1);

    public long getTime();

    public void setTime(long var1);

    public long getSeed();

    public void setSeed(long var1);

    public Vector3 getSpawn();

    public void setSpawn(Vector3 var1);

    public Map<Long, ? extends FullChunk> getLoadedChunks();

    public void doGarbageCollection();

    default public void doGarbageCollection(long l) {
    }

    public Level getLevel();

    public void close();

    public void saveLevelData();

    public void updateLevelName(String var1);

    public GameRules getGamerules();

    public void setGameRules(GameRules var1);
}

