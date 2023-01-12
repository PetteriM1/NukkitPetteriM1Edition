/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityEnderman;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;

public class EndermanBlockPickUpEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final Block b;

    public static HandlerList getHandlers() {
        return c;
    }

    public EndermanBlockPickUpEvent(EntityEnderman entityEnderman, Block block) {
        this.entity = entityEnderman;
        this.b = block;
    }

    public Block getBlock() {
        return this.b;
    }

    @Override
    public EntityEnderman getEntity() {
        return (EntityEnderman)super.getEntity();
    }
}

