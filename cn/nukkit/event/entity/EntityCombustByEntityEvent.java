/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityCombustEvent;

public class EntityCombustByEntityEvent
extends EntityCombustEvent {
    protected final Entity combuster;

    public EntityCombustByEntityEvent(Entity entity, Entity entity2, int n) {
        super(entity2, n);
        this.combuster = entity;
    }

    public Entity getCombuster() {
        return this.combuster;
    }
}

