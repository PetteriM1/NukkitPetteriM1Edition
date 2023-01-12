/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockStructureBlock
extends BlockSolid {
    @Override
    public int getId() {
        return 252;
    }

    @Override
    public double getHardness() {
        return -1.0;
    }

    @Override
    public double getResistance() {
        return 1.8E7;
    }

    @Override
    public String getName() {
        return "Structure Block";
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.LIGHT_GRAY_BLOCK_COLOR;
    }
}

