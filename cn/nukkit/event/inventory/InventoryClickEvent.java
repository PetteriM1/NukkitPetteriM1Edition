/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.inventory.InventoryEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;

public class InventoryClickEvent
extends InventoryEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final int f;
    private final Item e;
    private final Item d;
    private final Player b;

    public static HandlerList getHandlers() {
        return c;
    }

    public InventoryClickEvent(Player player, Inventory inventory, int n, Item item, Item item2) {
        super(inventory);
        this.f = n;
        this.e = item;
        this.d = item2;
        this.b = player;
    }

    public int getSlot() {
        return this.f;
    }

    public Item getSourceItem() {
        return this.e;
    }

    public Item getHeldItem() {
        return this.d;
    }

    public Player getPlayer() {
        return this.b;
    }
}

