/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairsWood;
import cn.nukkit.utils.BlockColor;

public class BlockStairsAcacia
extends BlockStairsWood {
    public BlockStairsAcacia() {
        this(0);
    }

    public BlockStairsAcacia(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 163;
    }

    @Override
    public String getName() {
        return "Acacia Wood Stairs";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
    }
}

