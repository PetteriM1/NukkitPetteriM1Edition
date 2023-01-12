/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.vehicle;

import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.vehicle.VehicleEvent;
import cn.nukkit.level.Location;

public class VehicleMoveEvent
extends VehicleEvent {
    private static final HandlerList e = new HandlerList();
    private final Location c;
    private final Location d;

    public static HandlerList getHandlers() {
        return e;
    }

    public VehicleMoveEvent(EntityVehicle entityVehicle, Location location, Location location2) {
        super(entityVehicle);
        this.c = location;
        this.d = location2;
    }

    public Location getFrom() {
        return this.c;
    }

    public Location getTo() {
        return this.d;
    }
}

