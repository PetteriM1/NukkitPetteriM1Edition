/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockPressurePlateWood;
import cn.nukkit.utils.BlockColor;

public class BlockPressurePlateDarkOak
extends BlockPressurePlateWood {
    public BlockPressurePlateDarkOak() {
        this(0);
    }

    public BlockPressurePlateDarkOak(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Dark Oak Pressure Plate";
    }

    @Override
    public int getId() {
        return 407;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.BROWN_BLOCK_COLOR;
    }
}

