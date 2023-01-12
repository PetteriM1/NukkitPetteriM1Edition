/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.level;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.level.LevelEvent;
import cn.nukkit.level.Level;

public class LevelUnloadEvent
extends LevelEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();

    public static HandlerList getHandlers() {
        return c;
    }

    public LevelUnloadEvent(Level level) {
        super(level);
    }
}

