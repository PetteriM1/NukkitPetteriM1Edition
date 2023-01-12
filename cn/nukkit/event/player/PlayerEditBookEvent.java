/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.BookEditPacket;

public class PlayerEditBookEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final Item c;
    private final BookEditPacket.Action d;
    private Item e;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerEditBookEvent(Player player, Item item, Item item2, BookEditPacket.Action action) {
        this.player = player;
        this.c = item;
        this.e = item2;
        this.d = action;
    }

    public BookEditPacket.Action getAction() {
        return this.d;
    }

    public Item getOldBook() {
        return this.c;
    }

    public Item getNewBook() {
        return this.e;
    }

    public void setNewBook(Item item) {
        this.e = item;
    }
}

