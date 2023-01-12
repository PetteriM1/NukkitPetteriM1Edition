/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedWhite
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedWhite() {
        this(0);
    }

    public BlockTerracottaGlazedWhite(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 220;
    }

    @Override
    public String getName() {
        return "White Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.WHITE;
    }
}

