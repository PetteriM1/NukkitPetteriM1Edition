/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.level;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.level.WeatherEvent;
import cn.nukkit.level.Level;

public class ThunderChangeEvent
extends WeatherEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final boolean d;

    public static HandlerList getHandlers() {
        return c;
    }

    public ThunderChangeEvent(Level level, boolean bl) {
        super(level);
        this.d = bl;
    }

    public boolean toThunderState() {
        return this.d;
    }
}

