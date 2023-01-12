/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockButtonWooden;

public class BlockButtonBirch
extends BlockButtonWooden {
    public BlockButtonBirch() {
        this(0);
    }

    public BlockButtonBirch(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Birch Button";
    }

    @Override
    public int getId() {
        return 396;
    }
}

