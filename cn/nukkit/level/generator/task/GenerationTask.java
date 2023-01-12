/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.task;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.SimpleChunkManager;
import cn.nukkit.scheduler.AsyncTask;

public class GenerationTask
extends AsyncTask {
    private final Level d;
    public boolean state = true;
    private BaseFullChunk e;

    public GenerationTask(Level level, BaseFullChunk baseFullChunk) {
        this.e = baseFullChunk;
        this.d = level;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onRun() {
        Generator generator = this.d.getGenerator();
        this.state = false;
        if (generator == null) {
            Server.getInstance().getLogger().debug("[GenerationTask] generator == null");
            return;
        }
        SimpleChunkManager simpleChunkManager = (SimpleChunkManager)generator.getChunkManager();
        if (simpleChunkManager == null) {
            this.state = false;
            return;
        }
        simpleChunkManager.cleanChunks(this.d.getSeed());
        SimpleChunkManager simpleChunkManager2 = simpleChunkManager;
        synchronized (simpleChunkManager2) {
            try {
                BaseFullChunk baseFullChunk = this.e;
                if (baseFullChunk == null) {
                    return;
                }
                BaseFullChunk baseFullChunk2 = baseFullChunk;
                synchronized (baseFullChunk2) {
                    if (!baseFullChunk.isGenerated()) {
                        simpleChunkManager.setChunk(baseFullChunk.getX(), baseFullChunk.getZ(), baseFullChunk);
                        generator.generateChunk(baseFullChunk.getX(), baseFullChunk.getZ());
                        baseFullChunk = simpleChunkManager.getChunk(baseFullChunk.getX(), baseFullChunk.getZ());
                        baseFullChunk.setGenerated();
                    }
                }
                this.e = baseFullChunk;
                this.state = true;
            }
            finally {
                simpleChunkManager.cleanChunks(this.d.getSeed());
            }
        }
    }

    @Override
    public void onCompletion(Server server) {
        if (this.d != null) {
            if (!this.state) {
                return;
            }
            BaseFullChunk baseFullChunk = this.e;
            if (baseFullChunk == null) {
                return;
            }
            this.d.generateChunkCallback(baseFullChunk.getX(), baseFullChunk.getZ(), baseFullChunk);
        }
    }
}

