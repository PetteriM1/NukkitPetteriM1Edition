/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;

public class BlockFadeEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final Block b;

    public static HandlerList getHandlers() {
        return c;
    }

    public BlockFadeEvent(Block block, Block block2) {
        super(block);
        this.b = block2;
    }

    public Block getNewState() {
        return this.b;
    }
}

