/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedCyan
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedCyan() {
        this(0);
    }

    public BlockTerracottaGlazedCyan(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 229;
    }

    @Override
    public String getName() {
        return "Cyan Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.CYAN;
    }
}

