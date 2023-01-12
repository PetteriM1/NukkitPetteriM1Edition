/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedPurple
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedPurple() {
        this(0);
    }

    public BlockTerracottaGlazedPurple(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 219;
    }

    @Override
    public String getName() {
        return "Purple Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.PURPLE;
    }
}

