/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockBlastFurnaceLit;

public class BlockBlastFurnace
extends BlockBlastFurnaceLit {
    public BlockBlastFurnace() {
        this(0);
    }

    public BlockBlastFurnace(int n) {
        super(n);
    }

    @Override
    public int getLightLevel() {
        return 0;
    }

    @Override
    public String getName() {
        return "Blast Furnace";
    }

    @Override
    public int getId() {
        return 451;
    }
}

