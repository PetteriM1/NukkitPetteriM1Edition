/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairsCobblestone;

public class BlockStairsMossyCobblestone
extends BlockStairsCobblestone {
    public BlockStairsMossyCobblestone() {
        this(0);
    }

    public BlockStairsMossyCobblestone(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Mossy Cobblestone Stairs";
    }

    @Override
    public int getId() {
        return 434;
    }
}

