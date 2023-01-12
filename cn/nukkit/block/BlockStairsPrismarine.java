/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairs;
import cn.nukkit.utils.BlockColor;

public class BlockStairsPrismarine
extends BlockStairs {
    public BlockStairsPrismarine() {
        this(0);
    }

    public BlockStairsPrismarine(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 257;
    }

    @Override
    public double getHardness() {
        return 0.8;
    }

    @Override
    public double getResistance() {
        return 6.0;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public String getName() {
        return "Prismarine Stairs";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CYAN_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

