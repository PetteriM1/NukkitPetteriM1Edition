/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.utils.BlockColor;

public class BlockCartographyTable
extends BlockSolid {
    @Override
    public String getName() {
        return "Cartography Table";
    }

    @Override
    public int getId() {
        return 455;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public double getResistance() {
        return 12.5;
    }

    @Override
    public double getHardness() {
        return 2.5;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    public int getBurnChance() {
        return 5;
    }
}

