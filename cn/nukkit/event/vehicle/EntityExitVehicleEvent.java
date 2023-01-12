/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.vehicle;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.vehicle.VehicleEvent;

public class EntityExitVehicleEvent
extends VehicleEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final Entity d;

    public static HandlerList getHandlers() {
        return c;
    }

    public EntityExitVehicleEvent(Entity entity, EntityVehicle entityVehicle) {
        super(entityVehicle);
        this.d = entity;
    }

    public Entity getEntity() {
        return this.d;
    }

    public boolean isPlayer() {
        return this.d instanceof Player;
    }
}

