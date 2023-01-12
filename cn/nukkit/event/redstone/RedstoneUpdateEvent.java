/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.redstone;

import cn.nukkit.block.Block;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockUpdateEvent;

public class RedstoneUpdateEvent
extends BlockUpdateEvent {
    private static final HandlerList c = new HandlerList();

    public static HandlerList getHandlers() {
        return c;
    }

    public RedstoneUpdateEvent(Block block) {
        super(block);
    }
}

