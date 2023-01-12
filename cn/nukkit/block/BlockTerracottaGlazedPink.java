/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedPink
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedPink() {
        this(0);
    }

    public BlockTerracottaGlazedPink(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 226;
    }

    @Override
    public String getName() {
        return "Pink Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.PINK;
    }
}

