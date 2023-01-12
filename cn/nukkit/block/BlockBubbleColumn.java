/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTransparentMeta;

public class BlockBubbleColumn
extends BlockTransparentMeta {
    public BlockBubbleColumn() {
        this(0);
    }

    public BlockBubbleColumn(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Bubble Column";
    }

    @Override
    public int getId() {
        return 415;
    }

    @Override
    public double getResistance() {
        return 100.0;
    }

    @Override
    public double getHardness() {
        return 0.0;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

