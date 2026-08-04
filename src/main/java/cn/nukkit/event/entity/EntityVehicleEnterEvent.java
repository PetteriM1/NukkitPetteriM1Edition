package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

public class EntityVehicleEnterEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final EntityVehicle vehicle;

    public EntityVehicleEnterEvent(Entity entity, EntityVehicle vehicle) {
        this.entity = entity;
        this.vehicle = vehicle;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public EntityVehicle getVehicle() {
        return vehicle;
    }
}
