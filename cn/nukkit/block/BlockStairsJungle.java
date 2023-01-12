/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairsWood;
import cn.nukkit.utils.BlockColor;

public class BlockStairsJungle
extends BlockStairsWood {
    public BlockStairsJungle() {
        this(0);
    }

    public BlockStairsJungle(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 136;
    }

    @Override
    public String getName() {
        return "Jungle Wood Stairs";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }
}

