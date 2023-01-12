/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;

public class BlockDriedKelpBlock
extends Block {
    @Override
    public String getName() {
        return "Dried Kelp Block";
    }

    @Override
    public int getId() {
        return 394;
    }

    @Override
    public int getToolType() {
        return 6;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public int getBurnAbility() {
        return 30;
    }
}

