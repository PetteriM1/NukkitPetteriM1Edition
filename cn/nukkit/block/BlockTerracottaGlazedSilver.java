/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedSilver
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedSilver() {
        this(0);
    }

    public BlockTerracottaGlazedSilver(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 228;
    }

    @Override
    public String getName() {
        return "Light Gray Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.LIGHT_GRAY;
    }
}

