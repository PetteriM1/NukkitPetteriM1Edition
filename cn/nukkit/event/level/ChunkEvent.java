/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.level;

import cn.nukkit.event.level.LevelEvent;
import cn.nukkit.level.format.FullChunk;

public abstract class ChunkEvent
extends LevelEvent {
    private final FullChunk c;

    public ChunkEvent(FullChunk fullChunk) {
        super(fullChunk.getProvider().getLevel());
        this.c = fullChunk;
    }

    public FullChunk getChunk() {
        return this.c;
    }
}

