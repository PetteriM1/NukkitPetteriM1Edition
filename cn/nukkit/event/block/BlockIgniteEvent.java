/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;

public class BlockIgniteEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    private final Block b;
    private final Entity c;
    private final BlockIgniteCause e;

    public static HandlerList getHandlers() {
        return d;
    }

    public BlockIgniteEvent(Block block, Block block2, Entity entity, BlockIgniteCause blockIgniteCause) {
        super(block);
        this.b = block2;
        this.c = entity;
        this.e = blockIgniteCause;
    }

    public Block getSource() {
        return this.b;
    }

    public Entity getEntity() {
        return this.c;
    }

    public BlockIgniteCause getCause() {
        return this.e;
    }

    public static enum BlockIgniteCause {
        EXPLOSION,
        FIREBALL,
        FLINT_AND_STEEL,
        LAVA,
        LIGHTNING,
        SPREAD;

    }
}

