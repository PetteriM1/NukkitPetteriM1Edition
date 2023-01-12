/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;

public class EntityInteractEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final Block b;

    public static HandlerList getHandlers() {
        return c;
    }

    public EntityInteractEvent(Entity entity, Block block) {
        this.entity = entity;
        this.b = block;
    }

    public Block getBlock() {
        return this.b;
    }
}

