/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.blockentity.BlockEntityBrewingStand;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;

public class BrewingInventory
extends ContainerInventory {
    public BrewingInventory(BlockEntityBrewingStand blockEntityBrewingStand) {
        super(blockEntityBrewingStand, InventoryType.BREWING_STAND);
    }

    @Override
    public BlockEntityBrewingStand getHolder() {
        return (BlockEntityBrewingStand)this.holder;
    }

    public Item getIngredient() {
        return this.getItem(0);
    }

    public void setIngredient(Item item) {
        this.setItem(0, item);
    }

    public void setFuel(Item item) {
        this.setItem(4, item);
    }

    public Item getFuel() {
        return this.getItem(4);
    }

    @Override
    public void onSlotChange(int n, Item item, boolean bl) {
        super.onSlotChange(n, item, bl);
        if (n >= 1 && n <= 3) {
            this.getHolder().updateBlock();
        }
        this.getHolder().scheduleUpdate();
    }
}

