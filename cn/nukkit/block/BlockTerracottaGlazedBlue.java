/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedBlue
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedBlue() {
        this(0);
    }

    public BlockTerracottaGlazedBlue(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 231;
    }

    @Override
    public String getName() {
        return "Blue Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.BLUE;
    }
}

