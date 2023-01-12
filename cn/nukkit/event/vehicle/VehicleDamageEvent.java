/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.vehicle;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.vehicle.VehicleEvent;

public class VehicleDamageEvent
extends VehicleEvent
implements Cancellable {
    private static final HandlerList e = new HandlerList();
    private final Entity c;
    private double d;

    public static HandlerList getHandlers() {
        return e;
    }

    public VehicleDamageEvent(EntityVehicle entityVehicle, Entity entity, double d2) {
        super(entityVehicle);
        this.c = entity;
        this.d = d2;
    }

    public Entity getAttacker() {
        return this.c;
    }

    public double getDamage() {
        return this.d;
    }

    public void setDamage(double d2) {
        this.d = d2;
    }
}

