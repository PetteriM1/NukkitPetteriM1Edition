/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedGreen
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedGreen() {
        this(0);
    }

    public BlockTerracottaGlazedGreen(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 233;
    }

    @Override
    public String getName() {
        return "Green Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.GREEN;
    }
}

