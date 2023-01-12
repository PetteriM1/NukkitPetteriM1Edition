/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemRedstoneComparator
extends Item {
    public ItemRedstoneComparator() {
        this((Integer)0);
    }

    public ItemRedstoneComparator(Integer n) {
        this((Integer)0, 1);
    }

    public ItemRedstoneComparator(Integer n, int n2) {
        super(404, n, n2, "Redstone Comparator");
        this.block = Block.get(149);
    }
}

