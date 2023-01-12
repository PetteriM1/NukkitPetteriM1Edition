/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;
import cn.nukkit.level.MovingObjectPosition;

public class ProjectileHitEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private MovingObjectPosition b;

    public static HandlerList getHandlers() {
        return c;
    }

    public ProjectileHitEvent(EntityProjectile entityProjectile) {
        this(entityProjectile, null);
    }

    public ProjectileHitEvent(EntityProjectile entityProjectile, MovingObjectPosition movingObjectPosition) {
        this.entity = entityProjectile;
        this.b = movingObjectPosition;
    }

    public MovingObjectPosition getMovingObjectPosition() {
        return this.b;
    }

    public void setMovingObjectPosition(MovingObjectPosition movingObjectPosition) {
        this.b = movingObjectPosition;
    }
}

