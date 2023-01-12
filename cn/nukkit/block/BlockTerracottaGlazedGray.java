/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedGray
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedGray() {
        this(0);
    }

    public BlockTerracottaGlazedGray(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 227;
    }

    @Override
    public String getName() {
        return "Gray Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.GRAY;
    }
}

