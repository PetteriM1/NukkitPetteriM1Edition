/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedLightBlue
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedLightBlue() {
        this(0);
    }

    public BlockTerracottaGlazedLightBlue(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 223;
    }

    @Override
    public String getName() {
        return "Light Blue Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.LIGHT_BLUE;
    }
}

