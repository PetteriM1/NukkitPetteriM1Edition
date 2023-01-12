/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;

public class EntityBlockChangeEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final Block c;
    private final Block d;

    public static HandlerList getHandlers() {
        return b;
    }

    public EntityBlockChangeEvent(Entity entity, Block block, Block block2) {
        this.entity = entity;
        this.c = block;
        this.d = block2;
    }

    public Block getFrom() {
        return this.c;
    }

    public Block getTo() {
        return this.d;
    }
}

