/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockWoodStripped;
import cn.nukkit.utils.BlockColor;

public class BlockWoodStrippedSpruce
extends BlockWoodStripped {
    @Override
    public String getName() {
        return "Stripped Spruce Log";
    }

    @Override
    public int getId() {
        return 260;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SPRUCE_BLOCK_COLOR;
    }
}

