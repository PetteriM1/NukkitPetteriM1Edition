/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockPressurePlateWood;
import cn.nukkit.utils.BlockColor;

public class BlockPressurePlateJungle
extends BlockPressurePlateWood {
    public BlockPressurePlateJungle() {
        this(0);
    }

    public BlockPressurePlateJungle(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Jungle Pressure Plate";
    }

    @Override
    public int getId() {
        return 408;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }
}

