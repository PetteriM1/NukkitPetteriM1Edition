/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;

public class EntityVehicleEnterEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final EntityVehicle c;

    public static HandlerList getHandlers() {
        return b;
    }

    public EntityVehicleEnterEvent(Entity entity, EntityVehicle entityVehicle) {
        this.entity = entity;
        this.c = entityVehicle;
    }

    public EntityVehicle getVehicle() {
        return this.c;
    }
}

