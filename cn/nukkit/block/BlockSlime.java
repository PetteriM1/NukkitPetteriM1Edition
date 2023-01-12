/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.utils.BlockColor;

public class BlockSlime
extends BlockSolid {
    @Override
    public double getHardness() {
        return 0.0;
    }

    @Override
    public String getName() {
        return "Slime Block";
    }

    @Override
    public int getId() {
        return 165;
    }

    @Override
    public double getResistance() {
        return 0.0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GREEN_BLOCK_COLOR;
    }
}

