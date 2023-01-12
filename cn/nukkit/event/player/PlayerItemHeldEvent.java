/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;

public class PlayerItemHeldEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final Item b;
    private final int d;

    public static HandlerList getHandlers() {
        return c;
    }

    public PlayerItemHeldEvent(Player player, Item item, int n) {
        this.player = player;
        this.b = item;
        this.d = n;
    }

    public int getSlot() {
        return this.d;
    }

    public int getInventorySlot() {
        return this.d;
    }

    public Item getItem() {
        return this.b;
    }
}

