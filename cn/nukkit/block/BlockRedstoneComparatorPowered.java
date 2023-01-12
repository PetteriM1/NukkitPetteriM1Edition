/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockRedstoneComparator;

public class BlockRedstoneComparatorPowered
extends BlockRedstoneComparator {
    public BlockRedstoneComparatorPowered() {
        this(0);
    }

    public BlockRedstoneComparatorPowered(int n) {
        super(n);
        this.isPowered = true;
    }

    @Override
    public int getId() {
        return 150;
    }

    @Override
    public String getName() {
        return "Comparator Block Powered";
    }

    @Override
    protected BlockRedstoneComparator getPowered() {
        return this;
    }
}

