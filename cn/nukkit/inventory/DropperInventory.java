/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.blockentity.BlockEntityDropper;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;

public class DropperInventory
extends ContainerInventory {
    public DropperInventory(BlockEntityDropper blockEntityDropper) {
        super(blockEntityDropper, InventoryType.DROPPER);
    }

    @Override
    public BlockEntityDropper getHolder() {
        return (BlockEntityDropper)super.getHolder();
    }
}

