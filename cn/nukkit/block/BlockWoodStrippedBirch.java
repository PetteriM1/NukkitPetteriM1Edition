/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockWoodStripped;
import cn.nukkit.utils.BlockColor;

public class BlockWoodStrippedBirch
extends BlockWoodStripped {
    @Override
    public String getName() {
        return "Stripped Birch Log";
    }

    @Override
    public int getId() {
        return 261;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }
}

