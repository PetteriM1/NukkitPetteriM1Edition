/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairsWood;
import cn.nukkit.utils.BlockColor;

public class BlockStairsDarkOak
extends BlockStairsWood {
    public BlockStairsDarkOak() {
        this(0);
    }

    public BlockStairsDarkOak(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 164;
    }

    @Override
    public String getName() {
        return "Dark Oak Wood Stairs";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.BROWN_BLOCK_COLOR;
    }
}

