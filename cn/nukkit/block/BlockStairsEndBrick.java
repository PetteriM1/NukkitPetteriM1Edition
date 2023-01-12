/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairs;

public class BlockStairsEndBrick
extends BlockStairs {
    public BlockStairsEndBrick() {
        this(0);
    }

    public BlockStairsEndBrick(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "End Brick Stairs";
    }

    @Override
    public int getId() {
        return 433;
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
    public boolean canHarvestWithHand() {
        return false;
    }
}

