/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTerracottaGlazed;
import cn.nukkit.utils.DyeColor;

public class BlockTerracottaGlazedLime
extends BlockTerracottaGlazed {
    public BlockTerracottaGlazedLime() {
        this(0);
    }

    public BlockTerracottaGlazedLime(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 225;
    }

    @Override
    public String getName() {
        return "Lime Glazed Terracotta";
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.LIME;
    }
}

