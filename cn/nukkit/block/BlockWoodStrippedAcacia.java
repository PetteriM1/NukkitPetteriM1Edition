/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockWoodStripped;
import cn.nukkit.utils.BlockColor;

public class BlockWoodStrippedAcacia
extends BlockWoodStripped {
    @Override
    public String getName() {
        return "Stripped Acacia Log";
    }

    @Override
    public int getId() {
        return 263;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
    }
}

