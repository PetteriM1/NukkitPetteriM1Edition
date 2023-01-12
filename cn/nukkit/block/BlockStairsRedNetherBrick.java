/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairsNetherBrick;

public class BlockStairsRedNetherBrick
extends BlockStairsNetherBrick {
    public BlockStairsRedNetherBrick() {
        this(0);
    }

    public BlockStairsRedNetherBrick(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Red Nether Brick Stairs";
    }

    @Override
    public int getId() {
        return 439;
    }
}

