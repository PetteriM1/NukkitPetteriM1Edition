/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockPistonHead;

public class BlockPistonHeadSticky
extends BlockPistonHead {
    public BlockPistonHeadSticky() {
        this(0);
    }

    public BlockPistonHeadSticky(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Sticky Piston Head";
    }

    @Override
    public int getId() {
        return 472;
    }
}

