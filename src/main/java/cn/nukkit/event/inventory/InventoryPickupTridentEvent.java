package cn.nukkit.event.inventory;

import cn.nukkit.entity.projectile.EntityThrownTrident;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.inventory.Inventory;

public class InventoryPickupTridentEvent extends InventoryEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final EntityThrownTrident trident;

    public InventoryPickupTridentEvent(Inventory inventory, EntityThrownTrident trident) {
        super(inventory);
        this.trident = trident;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public EntityThrownTrident getTrident() {
        return trident;
    }
}
