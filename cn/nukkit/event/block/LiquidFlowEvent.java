/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;

public class LiquidFlowEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final Block e;
    private final BlockLiquid d;
    private final int b;

    public static HandlerList getHandlers() {
        return c;
    }

    public LiquidFlowEvent(Block block, BlockLiquid blockLiquid, int n) {
        super(block);
        this.e = block;
        this.d = blockLiquid;
        this.b = n;
    }

    public int getNewFlowDecay() {
        return this.b;
    }

    public BlockLiquid getSource() {
        return this.d;
    }

    public Block getTo() {
        return this.e;
    }
}

