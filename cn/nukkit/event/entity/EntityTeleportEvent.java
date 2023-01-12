/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;
import cn.nukkit.level.Location;

public class EntityTeleportEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    private Location b;
    private Location c;

    public static HandlerList getHandlers() {
        return d;
    }

    public EntityTeleportEvent(Entity entity, Location location, Location location2) {
        this.entity = entity;
        this.b = location;
        this.c = location2;
    }

    public Location getFrom() {
        return this.b;
    }

    public void setFrom(Location location) {
        this.b = location;
    }

    public Location getTo() {
        return this.c;
    }

    public void setTo(Location location) {
        this.c = location;
    }
}

