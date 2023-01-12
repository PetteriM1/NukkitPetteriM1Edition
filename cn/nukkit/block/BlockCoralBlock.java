/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolidMeta;

public class BlockCoralBlock
extends BlockSolidMeta {
    public BlockCoralBlock() {
        this(0);
    }

    public BlockCoralBlock(int n) {
        super(n);
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public double getResistance() {
        return 6.0;
    }

    @Override
    public String getName() {
        return "Coral Block";
    }

    @Override
    public int getId() {
        return 387;
    }

    @Override
    public int getToolType() {
        return 3;
    }
}

