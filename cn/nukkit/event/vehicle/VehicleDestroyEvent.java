/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.vehicle;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.vehicle.VehicleEvent;

public class VehicleDestroyEvent
extends VehicleEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    private final Entity c;

    public static HandlerList getHandlers() {
        return d;
    }

    public VehicleDestroyEvent(EntityVehicle entityVehicle, Entity entity) {
        super(entityVehicle);
        this.c = entity;
    }

    public Entity getAttacker() {
        return this.c;
    }
}

