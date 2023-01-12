/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.blockentity.BlockEntityBrewingStand;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.inventory.InventoryEvent;
import cn.nukkit.item.Item;

public class BrewEvent
extends InventoryEvent
implements Cancellable {
    private static final HandlerList e = new HandlerList();
    private final BlockEntityBrewingStand f;
    private final Item d;
    private final Item[] c;
    private final int b;

    public static HandlerList getHandlers() {
        return e;
    }

    public BrewEvent(BlockEntityBrewingStand blockEntityBrewingStand) {
        super(blockEntityBrewingStand.getInventory());
        this.f = blockEntityBrewingStand;
        this.b = blockEntityBrewingStand.fuelAmount;
        this.d = blockEntityBrewingStand.getInventory().getIngredient();
        this.c = new Item[3];
        for (int k = 0; k < 3; ++k) {
            this.c[k] = blockEntityBrewingStand.getInventory().getItem(k);
        }
    }

    public BlockEntityBrewingStand getBrewingStand() {
        return this.f;
    }

    public Item getIngredient() {
        return this.d;
    }

    public Item[] getPotions() {
        return this.c;
    }

    public Item getPotion(int n) {
        return this.c[n];
    }

    public int getFuel() {
        return this.b;
    }
}

