/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.task;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.scheduler.AsyncTask;

public class LightPopulationTask
extends AsyncTask {
    public final int levelId;
    public BaseFullChunk chunk;

    public LightPopulationTask(Level level, BaseFullChunk baseFullChunk) {
        this.levelId = level.getId();
        this.chunk = baseFullChunk;
    }

    @Override
    public void onRun() {
        BaseFullChunk baseFullChunk = this.chunk.clone();
        if (baseFullChunk == null) {
            return;
        }
        baseFullChunk.recalculateHeightMap();
        baseFullChunk.populateSkyLight();
        baseFullChunk.setLightPopulated();
        this.chunk = baseFullChunk.clone();
    }

    @Override
    public void onCompletion(Server server) {
        Level level = server.getLevel(this.levelId);
        BaseFullChunk baseFullChunk = this.chunk.clone();
        if (level != null) {
            if (baseFullChunk == null) {
                return;
            }
            level.generateChunkCallback(baseFullChunk.getX(), baseFullChunk.getZ(), baseFullChunk);
        }
    }
}

