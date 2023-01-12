/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.weather;

import cn.nukkit.entity.weather.EntityLightningStrike;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.level.WeatherEvent;
import cn.nukkit.level.Level;

public class LightningStrikeEvent
extends WeatherEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final EntityLightningStrike d;

    public static HandlerList getHandlers() {
        return c;
    }

    public LightningStrikeEvent(Level level, EntityLightningStrike entityLightningStrike) {
        super(level);
        this.d = entityLightningStrike;
    }

    public EntityLightningStrike getLightning() {
        return this.d;
    }
}

