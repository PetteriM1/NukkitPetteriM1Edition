/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.inventory.InventoryEvent;
import cn.nukkit.inventory.Inventory;

public class InventoryPickupItemEvent
extends InventoryEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final EntityItem c;

    public static HandlerList getHandlers() {
        return b;
    }

    public InventoryPickupItemEvent(Inventory inventory, EntityItem entityItem) {
        super(inventory);
        this.c = entityItem;
    }

    public EntityItem getItem() {
        return this.c;
    }
}

