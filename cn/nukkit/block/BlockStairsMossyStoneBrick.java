/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairsStoneBrick;

public class BlockStairsMossyStoneBrick
extends BlockStairsStoneBrick {
    public BlockStairsMossyStoneBrick() {
        this(0);
    }

    public BlockStairsMossyStoneBrick(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Mossy Stone Brick Stairs";
    }

    @Override
    public int getId() {
        return 430;
    }
}

