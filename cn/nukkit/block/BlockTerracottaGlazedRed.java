/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedRed
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedRed() {
        this(0);
    }

    public BlockTerracottaGlazedRed(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 234;
    }

    @Override
    public String getName() {
        return "Red Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.RED;
    }
}

