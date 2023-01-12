/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.inventory.InventoryEvent;
import cn.nukkit.inventory.Inventory;

public class InventoryOpenEvent
extends InventoryEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final Player c;

    public static HandlerList getHandlers() {
        return b;
    }

    public InventoryOpenEvent(Inventory inventory, Player player) {
        super(inventory);
        this.c = player;
    }

    public Player getPlayer() {
        return this.c;
    }
}

