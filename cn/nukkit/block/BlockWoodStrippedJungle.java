/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockWoodStripped;
import cn.nukkit.utils.BlockColor;

public class BlockWoodStrippedJungle
extends BlockWoodStripped {
    @Override
    public String getName() {
        return "Stripped Jungle Log";
    }

    @Override
    public int getId() {
        return 262;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }
}

