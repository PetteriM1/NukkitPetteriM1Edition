/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.level;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.level.WeatherEvent;
import cn.nukkit.level.Level;

public class WeatherChangeEvent
extends WeatherEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    private final boolean c;

    public static HandlerList getHandlers() {
        return d;
    }

    public WeatherChangeEvent(Level level, boolean bl) {
        super(level);
        this.c = bl;
    }

    public boolean toWeatherState() {
        return this.c;
    }
}

