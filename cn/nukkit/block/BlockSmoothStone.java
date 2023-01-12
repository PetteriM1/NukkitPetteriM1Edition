/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;

public class BlockSmoothStone
extends BlockSolid {
    @Override
    public String getName() {
        return "Smooth Stone";
    }

    @Override
    public int getId() {
        return 438;
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public double getResistance() {
        return 10.0;
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

