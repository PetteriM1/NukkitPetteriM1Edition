/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.block.BlockWater;

public class BlockWaterStill
extends BlockWater {
    public BlockWaterStill() {
        super(0);
    }

    public BlockWaterStill(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 9;
    }

    @Override
    public String getName() {
        return "Still Water";
    }

    @Override
    public BlockLiquid getBlock(int n) {
        return (BlockLiquid)Block.get(9, n);
    }
}

