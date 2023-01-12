/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.level;

import cn.nukkit.event.HandlerList;
import cn.nukkit.event.level.ChunkEvent;
import cn.nukkit.level.format.FullChunk;

public class ChunkPopulateEvent
extends ChunkEvent {
    private static final HandlerList d = new HandlerList();

    public static HandlerList getHandlers() {
        return d;
    }

    public ChunkPopulateEvent(FullChunk fullChunk) {
        super(fullChunk);
    }
}

