/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedYellow
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedYellow() {
        this(0);
    }

    public BlockTerracottaGlazedYellow(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 224;
    }

    @Override
    public String getName() {
        return "Yellow Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.YELLOW;
    }
}

