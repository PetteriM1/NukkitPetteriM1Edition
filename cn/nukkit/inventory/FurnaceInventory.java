/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.blockentity.BlockEntityFurnace;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;

public class FurnaceInventory
extends ContainerInventory {
    public FurnaceInventory(BlockEntityFurnace blockEntityFurnace) {
        super(blockEntityFurnace, InventoryType.FURNACE);
    }

    public FurnaceInventory(BlockEntityFurnace blockEntityFurnace, InventoryType inventoryType) {
        super(blockEntityFurnace, inventoryType);
    }

    @Override
    public BlockEntityFurnace getHolder() {
        return (BlockEntityFurnace)this.holder;
    }

    public Item getResult() {
        return this.getItem(2);
    }

    public Item getFuel() {
        return this.getItem(1);
    }

    public Item getSmelting() {
        return this.getItem(0);
    }

    public boolean setResult(Item item) {
        return this.setItem(2, item);
    }

    public boolean setFuel(Item item) {
        return this.setItem(1, item);
    }

    public boolean setSmelting(Item item) {
        return this.setItem(0, item);
    }

    @Override
    public void onSlotChange(int n, Item item, boolean bl) {
        super.onSlotChange(n, item, bl);
        this.getHolder().scheduleUpdate();
    }
}

