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

public class PopulationTask
extends AsyncTask {
    private final long f;
    private final Level e;
    private boolean g = true;
    private BaseFullChunk h;
    private boolean d;
    public final BaseFullChunk[] chunks = new BaseFullChunk[9];

    public PopulationTask(Level level, BaseFullChunk baseFullChunk) {
        this.e = level;
        this.h = baseFullChunk;
        this.f = level.getSeed();
        this.chunks[4] = baseFullChunk;
        int n = 0;
        for (int k = -1; k <= 1; ++k) {
            int n2 = -1;
            while (n2 <= 1) {
                if (n != 4) {
                    BaseFullChunk baseFullChunk2;
                    this.chunks[n] = baseFullChunk2 = level.getChunk(baseFullChunk.getX() + n2, baseFullChunk.getZ() + k, true);
                }
                ++n2;
                ++n;
            }
        }
    }

    @Override
    public void onRun() {
        this.a(0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void a(int n) {
        if (n == this.chunks.length) {
            this.a();
        } else {
            BaseFullChunk baseFullChunk = this.chunks[n];
            if (baseFullChunk != null) {
                BaseFullChunk baseFullChunk2 = baseFullChunk;
                synchronized (baseFullChunk2) {
                    this.a(n + 1);
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void a() {
        this.g = false;
        Generator generator = this.e.getGenerator();
        if (generator == null) {
            Server.getInstance().getLogger().debug("[PopulationTask] generator == null");
            return;
        }
        SimpleChunkManager simpleChunkManager = (SimpleChunkManager)generator.getChunkManager();
        if (simpleChunkManager == null) {
            this.g = false;
            return;
        }
        SimpleChunkManager simpleChunkManager2 = simpleChunkManager;
        synchronized (simpleChunkManager2) {
            try {
                int n;
                simpleChunkManager.cleanChunks(this.f);
                BaseFullChunk baseFullChunk = this.h;
                if (baseFullChunk == null) {
                    return;
                }
                int n2 = 0;
                for (int k = -1; k < 2; ++k) {
                    n = -1;
                    while (n < 2) {
                        BaseFullChunk baseFullChunk2 = this.chunks[n2];
                        if (baseFullChunk2 != baseFullChunk) {
                            this.chunks[n2] = baseFullChunk2 == null ? this.e.getProvider().getEmptyChunk(baseFullChunk.getX() + k, baseFullChunk.getZ() + n) : baseFullChunk2;
                        }
                        ++n;
                        ++n2;
                    }
                }
                for (BaseFullChunk baseFullChunk3 : this.chunks) {
                    simpleChunkManager.setChunk(baseFullChunk3.getX(), baseFullChunk3.getZ(), baseFullChunk3);
                    if (baseFullChunk3.isGenerated()) continue;
                    generator.generateChunk(baseFullChunk3.getX(), baseFullChunk3.getZ());
                    BaseFullChunk baseFullChunk4 = simpleChunkManager.getChunk(baseFullChunk3.getX(), baseFullChunk3.getZ());
                    baseFullChunk4.setGenerated();
                    if (baseFullChunk4 == baseFullChunk3) continue;
                    simpleChunkManager.setChunk(baseFullChunk3.getX(), baseFullChunk3.getZ(), baseFullChunk4);
                }
                this.d = baseFullChunk.isPopulated();
                if (!this.d) {
                    generator.populateChunk(baseFullChunk.getX(), baseFullChunk.getZ());
                    baseFullChunk = simpleChunkManager.getChunk(baseFullChunk.getX(), baseFullChunk.getZ());
                    baseFullChunk.setPopulated();
                    baseFullChunk.recalculateHeightMap();
                    baseFullChunk.populateSkyLight();
                    baseFullChunk.setLightPopulated();
                    this.h = baseFullChunk;
                }
                simpleChunkManager.setChunk(baseFullChunk.getX(), baseFullChunk.getZ());
                n2 = 0;
                for (int k = -1; k < 2; ++k) {
                    n = -1;
                    while (n < 2) {
                        this.chunks[n2] = null;
                        BaseFullChunk baseFullChunk5 = simpleChunkManager.getChunk(baseFullChunk.getX() + k, baseFullChunk.getZ() + n);
                        if (baseFullChunk5 != null && baseFullChunk5.hasChanged()) {
                            this.chunks[n2] = baseFullChunk5;
                        }
                        ++n;
                        ++n2;
                    }
                }
                this.g = true;
            }
            finally {
                simpleChunkManager.cleanChunks(this.f);
            }
        }
    }

    @Override
    public void onCompletion(Server server) {
        if (this.e != null) {
            if (!this.g) {
                return;
            }
            BaseFullChunk baseFullChunk = this.h;
            if (baseFullChunk == null) {
                return;
            }
            for (BaseFullChunk baseFullChunk2 : this.chunks) {
                if (baseFullChunk2 == null) continue;
                this.e.generateChunkCallback(baseFullChunk2.getX(), baseFullChunk2.getZ(), baseFullChunk2);
            }
            this.e.generateChunkCallback(baseFullChunk.getX(), baseFullChunk.getZ(), baseFullChunk, this.d);
        }
    }
}

