/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator;

import cn.nukkit.Server;
import cn.nukkit.block.BlockID;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.DimensionData;
import cn.nukkit.level.DimensionEnum;
import cn.nukkit.level.generator.Normal;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import java.util.HashMap;
import java.util.Map;

public abstract class Generator
implements BlockID {
    public static final int TYPE_OLD = 0;
    public static final int TYPE_INFINITE = 1;
    public static final int TYPE_FLAT = 2;
    public static final int TYPE_NETHER = 3;
    public static final int TYPE_THE_END = 4;
    public static final int TYPE_VOID = 5;
    private static final Map<String, Class<? extends Generator>> b = new HashMap<String, Class<? extends Generator>>();
    private static final Map<Integer, Class<? extends Generator>> a = new HashMap<Integer, Class<? extends Generator>>();

    public abstract int getId();

    public DimensionData getDimensionData() {
        DimensionData dimensionData = DimensionEnum.getDataFromId(this.getDimension());
        if (dimensionData == null) {
            Server.getInstance().getLogger().warning("Invalid DimensionData for Generator " + this.getClass().getName());
            dimensionData = DimensionEnum.OVERWORLD.getDimensionData();
        }
        return dimensionData;
    }

    public int getDimension() {
        return 0;
    }

    public static boolean addGenerator(Class<? extends Generator> clazz, String string, int n) {
        string = string.toLowerCase();
        if (clazz != null && !b.containsKey(string)) {
            b.put(string, clazz);
            if (!a.containsKey(n)) {
                a.put(n, clazz);
            }
            return true;
        }
        return false;
    }

    public static String[] getGeneratorList() {
        String[] stringArray = new String[b.size()];
        return b.keySet().toArray(stringArray);
    }

    public static Class<? extends Generator> getGenerator(String string) {
        if (b.containsKey(string = string.toLowerCase())) {
            return b.get(string);
        }
        return Normal.class;
    }

    public static Class<? extends Generator> getGenerator(int n) {
        if (a.containsKey(n)) {
            return a.get(n);
        }
        return Normal.class;
    }

    public static String getGeneratorName(Class<? extends Generator> clazz) {
        for (Map.Entry<String, Class<? extends Generator>> entry : b.entrySet()) {
            if (entry.getValue() != clazz) continue;
            return entry.getKey();
        }
        return "unknown";
    }

    public static int getGeneratorType(Class<? extends Generator> clazz) {
        for (Map.Entry<Integer, Class<? extends Generator>> entry : a.entrySet()) {
            if (entry.getValue() != clazz) continue;
            return entry.getKey();
        }
        return 1;
    }

    public abstract void init(ChunkManager var1, NukkitRandom var2);

    public abstract void generateChunk(int var1, int var2);

    public abstract void populateChunk(int var1, int var2);

    public abstract Map<String, Object> getSettings();

    public abstract String getName();

    public abstract Vector3 getSpawn();

    public abstract ChunkManager getChunkManager();
}

