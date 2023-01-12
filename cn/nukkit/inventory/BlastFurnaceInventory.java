/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.blockentity.BlockEntityBlastFurnace;
import cn.nukkit.inventory.FurnaceInventory;
import cn.nukkit.inventory.InventoryType;

public class BlastFurnaceInventory
extends FurnaceInventory {
    public BlastFurnaceInventory(BlockEntityBlastFurnace blockEntityBlastFurnace) {
        super(blockEntityBlastFurnace, InventoryType.BLAST_FURNACE);
    }

    @Override
    public BlockEntityBlastFurnace getHolder() {
        return (BlockEntityBlastFurnace)this.holder;
    }
}

