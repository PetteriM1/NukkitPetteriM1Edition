/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemSeedsBeetroot
extends Item {
    public ItemSeedsBeetroot() {
        this((Integer)0, 1);
    }

    public ItemSeedsBeetroot(Integer n) {
        this(n, 1);
    }

    public ItemSeedsBeetroot(Integer n, int n2) {
        super(458, 0, n2, "Beetroot Seeds");
        this.block = Block.get(244);
    }
}

