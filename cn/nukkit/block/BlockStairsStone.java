/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairs;

public class BlockStairsStone
extends BlockStairs {
    public BlockStairsStone() {
        this(0);
    }

    public BlockStairsStone(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Stone Stairs";
    }

    @Override
    public int getId() {
        return 435;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public double getResistance() {
        return 30.0;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

