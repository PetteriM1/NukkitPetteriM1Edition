/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockShulkerBox;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;

public class BlockUndyedShulkerBox
extends BlockShulkerBox {
    public BlockUndyedShulkerBox() {
        super(0);
    }

    @Override
    public int getId() {
        return 205;
    }

    @Override
    public String getName() {
        return "Shulker Box";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.PURPLE_BLOCK_COLOR;
    }

    @Override
    public DyeColor getDyeColor() {
        return null;
    }

    @Override
    public void setDamage(int n) {
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        if (!(blockEntity instanceof InventoryHolder)) {
            return 0;
        }
        return ContainerInventory.calculateRedstone(((InventoryHolder)((Object)blockEntity)).getInventory());
    }
}

