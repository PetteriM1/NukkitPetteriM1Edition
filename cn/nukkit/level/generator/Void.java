/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import java.util.Map;

public class Void
extends Generator {
    private ChunkManager c;

    public Void() {
    }

    public Void(Map<String, Object> map) {
    }

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public ChunkManager getChunkManager() {
        return this.c;
    }

    @Override
    public void init(ChunkManager chunkManager, NukkitRandom nukkitRandom) {
        this.c = chunkManager;
    }

    @Override
    public void generateChunk(int n, int n2) {
    }

    @Override
    public void populateChunk(int n, int n2) {
    }

    @Override
    public Map<String, Object> getSettings() {
        return null;
    }

    @Override
    public String getName() {
        return "void";
    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(0.5, 64.0, 0.5);
    }
}

