/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.inventory.InventoryEvent;
import cn.nukkit.inventory.Inventory;

public class InventoryPickupArrowEvent
extends InventoryEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final EntityArrow b;

    public static HandlerList getHandlers() {
        return c;
    }

    public InventoryPickupArrowEvent(Inventory inventory, EntityArrow entityArrow) {
        super(inventory);
        this.b = entityArrow;
    }

    public EntityArrow getArrow() {
        return this.b;
    }
}

