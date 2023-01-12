/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockPressurePlateWood;
import cn.nukkit.utils.BlockColor;

public class BlockPressurePlateBirch
extends BlockPressurePlateWood {
    public BlockPressurePlateBirch() {
        this(0);
    }

    public BlockPressurePlateBirch(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Birch Pressure Plate";
    }

    @Override
    public int getId() {
        return 406;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }
}

