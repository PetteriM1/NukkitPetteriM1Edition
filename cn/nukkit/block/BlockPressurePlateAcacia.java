/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockPressurePlateWood;
import cn.nukkit.utils.BlockColor;

public class BlockPressurePlateAcacia
extends BlockPressurePlateWood {
    public BlockPressurePlateAcacia() {
        this(0);
    }

    public BlockPressurePlateAcacia(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Acacia Pressure Plate";
    }

    @Override
    public int getId() {
        return 405;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
    }
}

