/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockFormEvent;

public class BlockSpreadEvent
extends BlockFormEvent
implements Cancellable {
    private static final HandlerList e = new HandlerList();
    private final Block f;

    public static HandlerList getHandlers() {
        return e;
    }

    public BlockSpreadEvent(Block block, Block block2, Block block3) {
        super(block, block3);
        this.f = block2;
    }

    public Block getSource() {
        return this.f;
    }
}

