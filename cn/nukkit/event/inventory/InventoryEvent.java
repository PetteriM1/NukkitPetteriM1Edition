/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.Player;
import cn.nukkit.event.Event;
import cn.nukkit.inventory.Inventory;

public abstract class InventoryEvent
extends Event {
    protected final Inventory inventory;

    public InventoryEvent(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public Player[] getViewers() {
        return this.inventory.getViewers().toArray(new Player[0]);
    }
}

