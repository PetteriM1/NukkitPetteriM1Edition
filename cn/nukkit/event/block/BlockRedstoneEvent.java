/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;

public class BlockRedstoneEvent
extends BlockEvent {
    private static final HandlerList d = new HandlerList();
    private final int c;
    private final int b;

    public static HandlerList getHandlers() {
        return d;
    }

    public BlockRedstoneEvent(Block block, int n, int n2) {
        super(block);
        this.c = n;
        this.b = n2;
    }

    public int getOldPower() {
        return this.c;
    }

    public int getNewPower() {
        return this.b;
    }
}

