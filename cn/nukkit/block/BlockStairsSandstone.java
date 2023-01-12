/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairs;
import cn.nukkit.utils.BlockColor;

public class BlockStairsSandstone
extends BlockStairs {
    public BlockStairsSandstone() {
        this(0);
    }

    public BlockStairsSandstone(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 128;
    }

    @Override
    public double getHardness() {
        return 0.8;
    }

    @Override
    public double getResistance() {
        return 4.0;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public String getName() {
        return "Sandstone Stairs";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

