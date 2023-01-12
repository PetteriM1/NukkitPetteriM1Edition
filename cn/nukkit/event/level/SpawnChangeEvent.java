/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.level;

import cn.nukkit.event.HandlerList;
import cn.nukkit.event.level.LevelEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

public class SpawnChangeEvent
extends LevelEvent {
    private static final HandlerList d = new HandlerList();
    private final Position c;

    public static HandlerList getHandlers() {
        return d;
    }

    public SpawnChangeEvent(Level level, Position position) {
        super(level);
        this.c = position;
    }

    public Position getPreviousSpawn() {
        return this.c;
    }
}

