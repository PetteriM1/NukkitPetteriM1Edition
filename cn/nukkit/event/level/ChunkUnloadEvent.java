/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.level;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.level.ChunkEvent;
import cn.nukkit.level.format.FullChunk;

public class ChunkUnloadEvent
extends ChunkEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();

    public static HandlerList getHandlers() {
        return d;
    }

    public ChunkUnloadEvent(FullChunk fullChunk) {
        super(fullChunk);
    }
}

