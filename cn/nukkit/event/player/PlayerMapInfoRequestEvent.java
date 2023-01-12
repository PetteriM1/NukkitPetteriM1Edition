/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;

public class PlayerMapInfoRequestEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final Item c;

    public PlayerMapInfoRequestEvent(Player player, Item item) {
        this.player = player;
        this.c = item;
    }

    public Item getMap() {
        return this.c;
    }

    public static HandlerList getHandlers() {
        return b;
    }
}

