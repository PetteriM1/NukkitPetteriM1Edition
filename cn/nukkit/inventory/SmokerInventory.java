/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.blockentity.BlockEntitySmoker;
import cn.nukkit.inventory.FurnaceInventory;
import cn.nukkit.inventory.InventoryType;

public class SmokerInventory
extends FurnaceInventory {
    public SmokerInventory(BlockEntitySmoker blockEntitySmoker) {
        super(blockEntitySmoker, InventoryType.SMOKER);
    }

    @Override
    public BlockEntitySmoker getHolder() {
        return (BlockEntitySmoker)this.holder;
    }
}

