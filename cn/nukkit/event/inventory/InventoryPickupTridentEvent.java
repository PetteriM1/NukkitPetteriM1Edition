/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.entity.projectile.EntityThrownTrident;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.inventory.InventoryEvent;
import cn.nukkit.inventory.Inventory;

public class InventoryPickupTridentEvent
extends InventoryEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final EntityThrownTrident b;

    public static HandlerList getHandlers() {
        return c;
    }

    public InventoryPickupTridentEvent(Inventory inventory, EntityThrownTrident entityThrownTrident) {
        super(inventory);
        this.b = entityThrownTrident;
    }

    public EntityThrownTrident getTrident() {
        return this.b;
    }
}

