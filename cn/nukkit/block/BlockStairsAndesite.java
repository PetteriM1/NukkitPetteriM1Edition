/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairs;

public class BlockStairsAndesite
extends BlockStairs {
    public BlockStairsAndesite() {
        this(0);
    }

    public BlockStairsAndesite(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Andesite Stairs";
    }

    @Override
    public int getId() {
        return 426;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public double getHardness() {
        return 2.0;
    }

    @Override
    public double getResistance() {
        return 30.0;
    }
}

