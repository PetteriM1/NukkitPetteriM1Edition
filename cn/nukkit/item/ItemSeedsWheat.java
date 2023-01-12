/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemSeedsWheat
extends Item {
    public ItemSeedsWheat() {
        this((Integer)0, 1);
    }

    public ItemSeedsWheat(Integer n) {
        this(n, 1);
    }

    public ItemSeedsWheat(Integer n, int n2) {
        super(295, 0, n2, "Seeds");
        this.block = Block.get(59);
    }
}

