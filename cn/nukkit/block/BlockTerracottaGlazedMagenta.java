/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedMagenta
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedMagenta() {
        this(0);
    }

    public BlockTerracottaGlazedMagenta(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 222;
    }

    @Override
    public String getName() {
        return "Magenta Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.MAGENTA;
    }
}

