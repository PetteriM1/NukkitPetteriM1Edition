/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.level;

import cn.nukkit.event.Event;
import cn.nukkit.level.Level;

public abstract class WeatherEvent
extends Event {
    private final Level b;

    public WeatherEvent(Level level) {
        this.b = level;
    }

    public Level getLevel() {
        return this.b;
    }
}

