/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockRedstoneComparator;

public class BlockRedstoneComparatorUnpowered
extends BlockRedstoneComparator {
    public BlockRedstoneComparatorUnpowered() {
        this(0);
    }

    public BlockRedstoneComparatorUnpowered(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 149;
    }

    @Override
    public String getName() {
        return "Comparator Block Unpowered";
    }

    @Override
    protected BlockRedstoneComparator getUnpowered() {
        return this;
    }
}

