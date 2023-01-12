/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockPistonBase;

public class BlockPistonSticky
extends BlockPistonBase {
    public BlockPistonSticky() {
        this(0);
    }

    public BlockPistonSticky(int n) {
        super(n);
        this.sticky = true;
    }

    @Override
    public int getId() {
        return 29;
    }

    @Override
    public int getPistonHeadBlockId() {
        return 472;
    }

    @Override
    public String getName() {
        return "Sticky Piston";
    }
}

