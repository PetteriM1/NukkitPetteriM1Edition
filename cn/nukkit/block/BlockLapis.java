/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockLapis
extends BlockSolid {
    @Override
    public int getId() {
        return 22;
    }

    @Override
    public String getName() {
        return "Lapis Lazuli Block";
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public double getHardness() {
        return 3.0;
    }

    @Override
    public double getResistance() {
        return 5.0;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= 3) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.LAPIS_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

