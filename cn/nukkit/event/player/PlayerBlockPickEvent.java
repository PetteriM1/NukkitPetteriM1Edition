/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;

public class PlayerBlockPickEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final Block d;
    private Item c;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerBlockPickEvent(Player player, Block block, Item item) {
        this.d = block;
        this.c = item;
        this.player = player;
    }

    public Item getItem() {
        return this.c;
    }

    public void setItem(Item item) {
        this.c = item;
    }

    public Block getBlockClicked() {
        return this.d;
    }
}

