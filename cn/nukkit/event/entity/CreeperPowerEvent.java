/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.mob.EntityCreeper;
import cn.nukkit.entity.weather.EntityLightningStrike;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;

public class CreeperPowerEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final PowerCause d;
    private EntityLightningStrike b;

    public static HandlerList getHandlers() {
        return c;
    }

    public CreeperPowerEvent(EntityCreeper entityCreeper, EntityLightningStrike entityLightningStrike, PowerCause powerCause) {
        this(entityCreeper, powerCause);
        this.b = entityLightningStrike;
    }

    public CreeperPowerEvent(EntityCreeper entityCreeper, PowerCause powerCause) {
        this.entity = entityCreeper;
        this.d = powerCause;
    }

    @Override
    public EntityCreeper getEntity() {
        return (EntityCreeper)super.getEntity();
    }

    public EntityLightningStrike getLightning() {
        return this.b;
    }

    public PowerCause getCause() {
        return this.d;
    }

    public static enum PowerCause {
        LIGHTNING,
        SET_ON,
        SET_OFF;

    }
}

