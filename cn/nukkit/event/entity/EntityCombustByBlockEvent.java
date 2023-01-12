/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityCombustEvent;

public class EntityCombustByBlockEvent
extends EntityCombustEvent {
    protected final Block combuster;

    public EntityCombustByBlockEvent(Block block, Entity entity, int n) {
        super(entity, n);
        this.combuster = block;
    }

    public Block getCombuster() {
        return this.combuster;
    }
}

