/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedOrange
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedOrange() {
        this(0);
    }

    public BlockTerracottaGlazedOrange(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 221;
    }

    @Override
    public String getName() {
        return "Orange Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.ORANGE;
    }
}

