/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;

public class EntityExplosionPrimeEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private double d;
    private boolean c;

    public static HandlerList getHandlers() {
        return b;
    }

    public EntityExplosionPrimeEvent(Entity entity, double d2) {
        this.entity = entity;
        this.d = d2;
        this.c = true;
    }

    public double getForce() {
        return this.d;
    }

    public void setForce(double d2) {
        this.d = d2;
    }

    public boolean isBlockBreaking() {
        return this.c;
    }

    public void setBlockBreaking(boolean bl) {
        this.c = bl;
    }
}

