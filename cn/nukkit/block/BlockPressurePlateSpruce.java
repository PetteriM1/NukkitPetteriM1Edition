/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockPressurePlateWood;
import cn.nukkit.utils.BlockColor;

public class BlockPressurePlateSpruce
extends BlockPressurePlateWood {
    public BlockPressurePlateSpruce() {
        this(0);
    }

    public BlockPressurePlateSpruce(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Spruce Pressure Plate";
    }

    @Override
    public int getId() {
        return 409;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SPRUCE_BLOCK_COLOR;
    }
}

