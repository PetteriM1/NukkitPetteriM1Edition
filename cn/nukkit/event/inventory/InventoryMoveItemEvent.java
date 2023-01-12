/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.inventory.InventoryEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;

public class InventoryMoveItemEvent
extends InventoryEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final Inventory f;
    private final InventoryHolder d;
    private Item b;
    private final Action e;

    public static HandlerList getHandlers() {
        return c;
    }

    public InventoryMoveItemEvent(Inventory inventory, Inventory inventory2, InventoryHolder inventoryHolder, Item item, Action action) {
        super(inventory);
        this.f = inventory2;
        this.d = inventoryHolder;
        this.b = item;
        this.e = action;
    }

    public Inventory getTargetInventory() {
        return this.f;
    }

    public InventoryHolder getSource() {
        return this.d;
    }

    public Item getItem() {
        return this.b;
    }

    public void setItem(Item item) {
        this.b = item;
    }

    public Action getAction() {
        return this.e;
    }

    public static enum Action {
        SLOT_CHANGE,
        PICKUP,
        DROP,
        DISPENSE;

    }
}

