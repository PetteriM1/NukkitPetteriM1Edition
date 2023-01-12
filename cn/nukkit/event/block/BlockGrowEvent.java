/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;

public class BlockGrowEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final Block c;

    public static HandlerList getHandlers() {
        return b;
    }

    public BlockGrowEvent(Block block, Block block2) {
        super(block);
        this.c = block2;
    }

    public Block getNewState() {
        return this.c;
    }
}

