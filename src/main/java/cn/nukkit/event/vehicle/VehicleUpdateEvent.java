package cn.nukkit.event.vehicle;

import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.event.HandlerList;

public class VehicleUpdateEvent extends VehicleEvent {

    private static final HandlerList handlers = new HandlerList();

    public VehicleUpdateEvent(EntityVehicle vehicle) {
        super(vehicle);
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
