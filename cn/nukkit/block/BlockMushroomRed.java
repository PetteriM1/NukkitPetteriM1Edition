/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockMushroom;

public class BlockMushroomRed
extends BlockMushroom {
    public BlockMushroomRed() {
    }

    public BlockMushroomRed(int n) {
        super(0);
    }

    @Override
    public String getName() {
        return "Red Mushroom";
    }

    @Override
    public int getId() {
        return 40;
    }

    @Override
    protected int getType() {
        return 1;
    }
}

