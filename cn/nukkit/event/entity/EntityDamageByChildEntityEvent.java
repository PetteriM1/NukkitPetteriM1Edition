/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;

public class EntityDamageByChildEntityEvent
extends EntityDamageByEntityEvent {
    private final Entity j;

    public EntityDamageByChildEntityEvent(Entity entity, Entity entity2, Entity entity3, EntityDamageEvent.DamageCause damageCause, float f2) {
        this(entity, entity2, entity3, damageCause, f2, 0.3f);
    }

    public EntityDamageByChildEntityEvent(Entity entity, Entity entity2, Entity entity3, EntityDamageEvent.DamageCause damageCause, float f2, float f3) {
        super(entity, entity3, damageCause, f2, f3);
        this.j = entity2;
    }

    public Entity getChild() {
        return this.j;
    }
}

