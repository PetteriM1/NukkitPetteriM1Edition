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

public class EntityEnterVehicleEvent
extends VehicleEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    private final Entity c;

    public static HandlerList getHandlers() {
        return d;
    }

    public EntityEnterVehicleEvent(Entity entity, EntityVehicle entityVehicle) {
        super(entityVehicle);
        this.c = entity;
    }

    public Entity getEntity() {
        return this.c;
    }

    public boolean isPlayer() {
        return this.c instanceof Player;
    }
}

