/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.blockentity.BlockEntityDispenser;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;

public class DispenserInventory
extends ContainerInventory {
    public DispenserInventory(BlockEntityDispenser blockEntityDispenser) {
        super(blockEntityDispenser, InventoryType.DISPENSER);
    }

    @Override
    public BlockEntityDispenser getHolder() {
        return (BlockEntityDispenser)super.getHolder();
    }
}

