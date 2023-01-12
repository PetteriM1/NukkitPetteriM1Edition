/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairsAndesite;

public class BlockStairsAndesitePolished
extends BlockStairsAndesite {
    public BlockStairsAndesitePolished() {
        this(0);
    }

    public BlockStairsAndesitePolished(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Polished Andesite Stairs";
    }

    @Override
    public int getId() {
        return 429;
    }
}

