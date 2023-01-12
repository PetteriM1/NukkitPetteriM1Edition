/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.utils.BlockColor;

public class BlockHoneycombBlock
extends BlockSolid {
    @Override
    public String getName() {
        return "Honeycomb Block";
    }

    @Override
    public int getId() {
        return 476;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public double getResistance() {
        return 3.0;
    }

    @Override
    public int getToolType() {
        return 0;
    }
}

