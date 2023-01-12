/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;

public class EntityDamageByBlockEvent
extends EntityDamageEvent {
    private final Block g;

    public EntityDamageByBlockEvent(Block block, Entity entity, EntityDamageEvent.DamageCause damageCause, float f2) {
        super(entity, damageCause, f2);
        this.g = block;
    }

    public Block getDamager() {
        return this.g;
    }
}

