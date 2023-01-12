/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerBedLeaveEvent
extends PlayerEvent {
    private static final HandlerList b = new HandlerList();
    private final Block c;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerBedLeaveEvent(Player player, Block block) {
        this.player = player;
        this.c = block;
    }

    public Block getBed() {
        return this.c;
    }
}

