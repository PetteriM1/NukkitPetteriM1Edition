/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairs;

public class BlockStairsStoneBrick
extends BlockStairs {
    public BlockStairsStoneBrick() {
        this(0);
    }

    public BlockStairsStoneBrick(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 109;
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
    public String getName() {
        return "Stone Brick Stairs";
    }
}

