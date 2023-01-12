/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlower;

public class BlockDandelion
extends BlockFlower {
    public BlockDandelion() {
        this(0);
    }

    public BlockDandelion(int n) {
        super(0);
    }

    @Override
    public String getName() {
        return "Dandelion";
    }

    @Override
    public int getId() {
        return 37;
    }

    @Override
    protected Block getUncommonFlower() {
        return BlockDandelion.get(38);
    }
}

