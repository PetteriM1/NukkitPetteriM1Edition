/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;

public class PlayerDropItemEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final Item b;

    public static HandlerList getHandlers() {
        return c;
    }

    public PlayerDropItemEvent(Player player, Item item) {
        this.player = player;
        this.b = item;
    }

    public Item getItem() {
        return this.b;
    }
}

