/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.vehicle;

import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.vehicle.VehicleEvent;

public class VehicleUpdateEvent
extends VehicleEvent {
    private static final HandlerList c = new HandlerList();

    public static HandlerList getHandlers() {
        return c;
    }

    public VehicleUpdateEvent(EntityVehicle entityVehicle) {
        super(entityVehicle);
    }
}

