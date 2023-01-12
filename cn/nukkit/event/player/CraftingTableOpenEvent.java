/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class CraftingTableOpenEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final Block b;

    public static HandlerList getHandlers() {
        return c;
    }

    public CraftingTableOpenEvent(Player player, Block block) {
        this.player = player;
        this.b = block;
    }

    public Block getCraftingTable() {
        return this.b;
    }
}

