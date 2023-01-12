/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.inventory.InventoryEvent;
import cn.nukkit.inventory.EnchantInventory;
import cn.nukkit.item.Item;

public class EnchantItemEvent
extends InventoryEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private Item c;
    private Item f;
    private int d;
    private Player e;

    public static HandlerList getHandlers() {
        return b;
    }

    public EnchantItemEvent(EnchantInventory enchantInventory, Item item, Item item2, int n, Player player) {
        super(enchantInventory);
        this.c = item;
        this.f = item2;
        this.d = n;
        this.e = player;
    }

    public Item getOldItem() {
        return this.c;
    }

    public Item getNewItem() {
        return this.f;
    }

    public int getXpCost() {
        return this.d;
    }

    public Player getEnchanter() {
        return this.e;
    }

    public void setOldItem(Item item) {
        this.c = item;
    }

    public void setNewItem(Item item) {
        this.f = item;
    }

    public void setXpCost(int n) {
        this.d = n;
    }

    public void setEnchanter(Player player) {
        this.e = player;
    }
}

