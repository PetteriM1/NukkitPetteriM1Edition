/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockGrowEvent;

public class BlockFormEvent
extends BlockGrowEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();

    public static HandlerList getHandlers() {
        return d;
    }

    public BlockFormEvent(Block block, Block block2) {
        super(block, block2);
    }
}

