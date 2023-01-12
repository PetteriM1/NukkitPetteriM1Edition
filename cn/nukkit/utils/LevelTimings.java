/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.level.Level;
import co.aikar.timings.Timing;
import co.aikar.timings.TimingsManager;

public class LevelTimings {
    public final Timing doChunkUnload;
    public final Timing doTickPending;
    public final Timing doChunkGC;
    public final Timing doTick;
    public final Timing tickChunks;
    public final Timing entityTick;
    public final Timing blockEntityTick;
    public final Timing syncChunkSendTimer;
    public final Timing syncChunkSendPrepareTimer;
    public final Timing syncChunkLoadTimer;
    public final Timing syncChunkLoadDataTimer;
    public final Timing syncChunkLoadEntitiesTimer;
    public final Timing syncChunkLoadBlockEntitiesTimer;

    public LevelTimings(Level level) {
        String string = level.getFolderName() + " - ";
        this.doChunkUnload = TimingsManager.getTiming(string + "doChunkUnload");
        this.doTickPending = TimingsManager.getTiming(string + "doTickPending");
        this.doChunkGC = TimingsManager.getTiming(string + "doChunkGC");
        this.doTick = TimingsManager.getTiming(string + "doTick");
        this.tickChunks = TimingsManager.getTiming(string + "tickChunks");
        this.entityTick = TimingsManager.getTiming(string + "entityTick");
        this.blockEntityTick = TimingsManager.getTiming(string + "blockEntityTick");
        this.syncChunkSendTimer = TimingsManager.getTiming(string + "syncChunkSend");
        this.syncChunkSendPrepareTimer = TimingsManager.getTiming(string + "syncChunkSendPrepare");
        this.syncChunkLoadTimer = TimingsManager.getTiming(string + "syncChunkLoad");
        this.syncChunkLoadDataTimer = TimingsManager.getTiming(string + "syncChunkLoad - Data");
        this.syncChunkLoadEntitiesTimer = TimingsManager.getTiming(string + "syncChunkLoad - Entities");
        this.syncChunkLoadBlockEntitiesTimer = TimingsManager.getTiming(string + "syncChunkLoad - BlockEntities");
    }
}

