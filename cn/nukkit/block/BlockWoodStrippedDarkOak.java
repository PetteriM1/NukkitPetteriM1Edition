/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockWoodStripped;
import cn.nukkit.utils.BlockColor;

public class BlockWoodStrippedDarkOak
extends BlockWoodStripped {
    @Override
    public String getName() {
        return "Stripped Dark Oak Log";
    }

    @Override
    public int getId() {
        return 264;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.BROWN_BLOCK_COLOR;
    }
}

