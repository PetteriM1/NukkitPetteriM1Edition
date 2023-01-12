/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityEvent;

public class EntityDamageBlockedEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList e = new HandlerList();
    private final EntityDamageEvent d;
    private boolean b;
    private boolean c;

    public static HandlerList getHandlers() {
        return e;
    }

    public EntityDamageBlockedEvent(Entity entity, EntityDamageEvent entityDamageEvent, boolean bl, boolean bl2) {
        this.entity = entity;
        this.d = entityDamageEvent;
        this.b = bl;
        this.c = bl2;
    }

    public EntityDamageEvent.DamageCause getCause() {
        return this.d.getCause();
    }

    public Entity getAttacker() {
        if (this.d instanceof EntityDamageByEntityEvent) {
            return ((EntityDamageByEntityEvent)this.d).getDamager();
        }
        return this.d.getEntity();
    }

    public EntityDamageEvent getDamage() {
        return this.d;
    }

    public boolean getKnockBackAttacker() {
        return this.b;
    }

    public boolean getAnimation() {
        return this.c;
    }

    public void setKnockBackAttacker(boolean bl) {
        this.b = bl;
    }

    public void setAnimation(boolean bl) {
        this.c = bl;
    }
}

