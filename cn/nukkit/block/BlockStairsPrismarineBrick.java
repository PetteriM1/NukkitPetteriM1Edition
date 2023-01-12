/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairsPrismarine;
import cn.nukkit.utils.BlockColor;

public class BlockStairsPrismarineBrick
extends BlockStairsPrismarine {
    public BlockStairsPrismarineBrick() {
        this(0);
    }

    public BlockStairsPrismarineBrick(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 259;
    }

    @Override
    public String getName() {
        return "Prismarine Brick Stairs";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIAMOND_BLOCK_COLOR;
    }
}

