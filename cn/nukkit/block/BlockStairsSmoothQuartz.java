/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairsQuartz;

public class BlockStairsSmoothQuartz
extends BlockStairsQuartz {
    public BlockStairsSmoothQuartz() {
        this(0);
    }

    public BlockStairsSmoothQuartz(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Smooth Quartz Stairs";
    }

    @Override
    public int getId() {
        return 440;
    }
}

