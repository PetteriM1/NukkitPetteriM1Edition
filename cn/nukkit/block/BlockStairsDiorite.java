/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairs;
import cn.nukkit.utils.BlockColor;

public class BlockStairsDiorite
extends BlockStairs {
    public BlockStairsDiorite() {
        this(0);
    }

    public BlockStairsDiorite(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Diorite Stairs";
    }

    @Override
    public int getId() {
        return 425;
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
        return BlockColor.QUARTZ_BLOCK_COLOR;
    }
}

