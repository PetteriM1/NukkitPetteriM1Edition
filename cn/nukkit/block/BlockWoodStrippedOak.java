/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockWoodStripped;
import cn.nukkit.utils.BlockColor;

public class BlockWoodStrippedOak
extends BlockWoodStripped {
    @Override
    public String getName() {
        return "Stripped Oak Log";
    }

    @Override
    public int getId() {
        return 265;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }
}

