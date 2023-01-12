/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.blockentity.BlockEntityBrewingStand;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.inventory.InventoryEvent;
import cn.nukkit.item.Item;

public class StartBrewEvent
extends InventoryEvent
implements Cancellable {
    private static final HandlerList e = new HandlerList();
    private final BlockEntityBrewingStand c;
    private final Item d;
    private final Item[] b;

    public static HandlerList getHandlers() {
        return e;
    }

    public StartBrewEvent(BlockEntityBrewingStand blockEntityBrewingStand) {
        super(blockEntityBrewingStand.getInventory());
        this.c = blockEntityBrewingStand;
        this.d = blockEntityBrewingStand.getInventory().getIngredient();
        this.b = new Item[3];
        for (int k = 0; k < 3; ++k) {
            this.b[k] = blockEntityBrewingStand.getInventory().getItem(k);
        }
    }

    public BlockEntityBrewingStand getBrewingStand() {
        return this.c;
    }

    public Item getIngredient() {
        return this.d;
    }

    public Item[] getPotions() {
        return this.b;
    }

    public Item getPotion(int n) {
        return this.b[n];
    }
}

