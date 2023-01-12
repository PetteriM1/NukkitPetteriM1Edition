/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.level;

import cn.nukkit.event.HandlerList;
import cn.nukkit.event.level.ChunkEvent;
import cn.nukkit.level.format.FullChunk;

public class ChunkLoadEvent
extends ChunkEvent {
    private static final HandlerList e = new HandlerList();
    private final boolean d;

    public static HandlerList getHandlers() {
        return e;
    }

    public ChunkLoadEvent(FullChunk fullChunk, boolean bl) {
        super(fullChunk);
        this.d = bl;
    }

    public boolean isNewChunk() {
        return this.d;
    }
}

