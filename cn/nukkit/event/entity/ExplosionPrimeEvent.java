/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;

public class ExplosionPrimeEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    protected double force;
    private boolean b;

    public static HandlerList getHandlers() {
        return c;
    }

    public ExplosionPrimeEvent(Entity entity, double d2) {
        this.entity = entity;
        this.force = d2;
        this.b = true;
    }

    public double getForce() {
        return this.force;
    }

    public void setForce(double d2) {
        this.force = d2;
    }

    public boolean isBlockBreaking() {
        return this.b;
    }

    public void setBlockBreaking(boolean bl) {
        this.b = bl;
    }
}

