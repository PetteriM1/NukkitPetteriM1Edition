/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;

public class BlockPistonChangeEvent
extends BlockEvent {
    private static final HandlerList d = new HandlerList();
    private final int b;
    private final int c;

    public static HandlerList getHandlers() {
        return d;
    }

    public BlockPistonChangeEvent(Block block, int n, int n2) {
        super(block);
        this.b = n;
        this.c = n2;
    }

    public int getOldPower() {
        return this.b;
    }

    public int getNewPower() {
        return this.c;
    }
}

