/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedBlack
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedBlack() {
        this(0);
    }

    public BlockTerracottaGlazedBlack(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 235;
    }

    @Override
    public String getName() {
        return "Black Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.BLACK;
    }
}

