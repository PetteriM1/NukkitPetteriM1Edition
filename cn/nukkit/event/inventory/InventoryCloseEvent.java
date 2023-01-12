/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.inventory.InventoryEvent;
import cn.nukkit.inventory.Inventory;

public class InventoryCloseEvent
extends InventoryEvent {
    private static final HandlerList c = new HandlerList();
    private final Player b;

    public static HandlerList getHandlers() {
        return c;
    }

    public InventoryCloseEvent(Inventory inventory, Player player) {
        super(inventory);
        this.b = player;
    }

    public Player getPlayer() {
        return this.b;
    }
}

