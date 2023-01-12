/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLava;
import cn.nukkit.block.BlockLiquid;

public class BlockLavaStill
extends BlockLava {
    public BlockLavaStill() {
        super(0);
    }

    public BlockLavaStill(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 11;
    }

    @Override
    public String getName() {
        return "Still Lava";
    }

    @Override
    public BlockLiquid getBlock(int n) {
        return (BlockLiquid)Block.get(11, n);
    }
}

