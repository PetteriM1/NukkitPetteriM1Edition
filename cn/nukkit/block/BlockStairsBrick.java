/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairs;
import cn.nukkit.utils.BlockColor;

public class BlockStairsBrick
extends BlockStairs {
    public BlockStairsBrick() {
        this(0);
    }

    public BlockStairsBrick(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 108;
    }

    @Override
    public double getHardness() {
        return 2.0;
    }

    @Override
    public double getResistance() {
        return 30.0;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public String getName() {
        return "Brick Stairs";
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.RED_BLOCK_COLOR;
    }
}

