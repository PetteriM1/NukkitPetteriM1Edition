/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairs;
import cn.nukkit.utils.BlockColor;

public class BlockStairsGranite
extends BlockStairs {
    public BlockStairsGranite() {
        this(0);
    }

    public BlockStairsGranite(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Granite Stairs";
    }

    @Override
    public int getId() {
        return 424;
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

    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }
}

