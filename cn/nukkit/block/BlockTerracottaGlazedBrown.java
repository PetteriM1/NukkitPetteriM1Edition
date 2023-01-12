/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedBrown
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedBrown() {
        this(0);
    }

    public BlockTerracottaGlazedBrown(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 232;
    }

    @Override
    public String getName() {
        return "Brown Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.BROWN;
    }
}

