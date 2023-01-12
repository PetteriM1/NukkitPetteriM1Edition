/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairsWood;
import cn.nukkit.utils.BlockColor;

public class BlockStairsSpruce
extends BlockStairsWood {
    public BlockStairsSpruce() {
        this(0);
    }

    public BlockStairsSpruce(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 134;
    }

    @Override
    public String getName() {
        return "Spruce Wood Stairs";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SPRUCE_BLOCK_COLOR;
    }
}

