/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairsWood;
import cn.nukkit.utils.BlockColor;

public class BlockStairsBirch
extends BlockStairsWood {
    public BlockStairsBirch() {
        this(0);
    }

    public BlockStairsBirch(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 135;
    }

    @Override
    public String getName() {
        return "Birch Wood Stairs";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }
}

