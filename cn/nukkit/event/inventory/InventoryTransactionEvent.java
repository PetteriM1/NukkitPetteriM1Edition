/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.inventory.transaction.InventoryTransaction;

public class InventoryTransactionEvent
extends Event
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final InventoryTransaction b;

    public static HandlerList getHandlers() {
        return c;
    }

    public InventoryTransactionEvent(InventoryTransaction inventoryTransaction) {
        this.b = inventoryTransaction;
    }

    public InventoryTransaction getTransaction() {
        return this.b;
    }
}

