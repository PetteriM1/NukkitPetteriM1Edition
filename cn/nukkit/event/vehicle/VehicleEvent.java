/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.vehicle;

import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.event.Event;

public abstract class VehicleEvent
extends Event {
    private final EntityVehicle b;

    public VehicleEvent(EntityVehicle entityVehicle) {
        this.b = entityVehicle;
    }

    public EntityVehicle getVehicle() {
        return this.b;
    }
}

