/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.inventory.InventoryEvent;
import cn.nukkit.inventory.AnvilInventory;
import cn.nukkit.item.Item;

public class RepairItemEvent
extends InventoryEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    private final Item b;
    private final Item f;
    private final Item e;
    private int g;
    private final Player c;

    public static HandlerList getHandlers() {
        return d;
    }

    public RepairItemEvent(AnvilInventory anvilInventory, Item item, Item item2, Item item3, int n, Player player) {
        super(anvilInventory);
        this.b = item;
        this.f = item2;
        this.e = item3;
        this.g = n;
        this.c = player;
    }

    public Item getOldItem() {
        return this.b;
    }

    public Item getNewItem() {
        return this.f;
    }

    public Item getMaterialItem() {
        return this.e;
    }

    public int getCost() {
        return this.g;
    }

    public void setCost(int n) {
        this.g = n;
    }

    public Player getPlayer() {
        return this.c;
    }
}

