/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;
import cn.nukkit.item.Item;

public class EntityShootBowEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final Item e;
    private EntityProjectile b;
    private double d;

    public static HandlerList getHandlers() {
        return c;
    }

    public EntityShootBowEvent(EntityLiving entityLiving, Item item, EntityProjectile entityProjectile, double d2) {
        this.entity = entityLiving;
        this.e = item;
        this.b = entityProjectile;
        this.d = d2;
    }

    @Override
    public EntityLiving getEntity() {
        return (EntityLiving)this.entity;
    }

    public Item getBow() {
        return this.e;
    }

    public EntityProjectile getProjectile() {
        return this.b;
    }

    public void setProjectile(Entity entity) {
        if (entity != this.b) {
            if (this.b.getViewers().isEmpty()) {
                this.b.close();
            }
            this.b = (EntityProjectile)entity;
        }
    }

    public double getForce() {
        return this.d;
    }

    public void setForce(double d2) {
        this.d = d2;
    }
}

