/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockPistonBase;

public class BlockPiston
extends BlockPistonBase {
    public BlockPiston() {
        this(0);
    }

    public BlockPiston(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 33;
    }

    @Override
    public int getPistonHeadBlockId() {
        return 34;
    }

    @Override
    public String getName() {
        return "Piston";
    }
}

