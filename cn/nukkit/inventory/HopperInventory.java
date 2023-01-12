/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.blockentity.BlockEntityHopper;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;

public class HopperInventory
extends ContainerInventory {
    public HopperInventory(BlockEntityHopper blockEntityHopper) {
        super(blockEntityHopper, InventoryType.HOPPER);
    }

    @Override
    public BlockEntityHopper getHolder() {
        return (BlockEntityHopper)super.getHolder();
    }
}

