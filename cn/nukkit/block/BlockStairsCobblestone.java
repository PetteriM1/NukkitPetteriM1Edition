/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairs;

public class BlockStairsCobblestone
extends BlockStairs {
    public BlockStairsCobblestone() {
        this(0);
    }

    public BlockStairsCobblestone(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 67;
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
        return "Cobblestone Stairs";
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

