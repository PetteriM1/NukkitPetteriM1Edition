/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockMushroom;

public class BlockMushroomBrown
extends BlockMushroom {
    public BlockMushroomBrown() {
    }

    public BlockMushroomBrown(int n) {
        super(0);
    }

    @Override
    public String getName() {
        return "Brown Mushroom";
    }

    @Override
    public int getId() {
        return 39;
    }

    @Override
    public int getLightLevel() {
        return 1;
    }

    @Override
    protected int getType() {
        return 0;
    }
}

