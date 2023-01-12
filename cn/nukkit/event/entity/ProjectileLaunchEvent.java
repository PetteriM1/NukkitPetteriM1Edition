/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;

public class ProjectileLaunchEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();

    public static HandlerList getHandlers() {
        return b;
    }

    public ProjectileLaunchEvent(EntityProjectile entityProjectile) {
        this.entity = entityProjectile;
    }

    @Override
    public EntityProjectile getEntity() {
        return (EntityProjectile)this.entity;
    }
}

