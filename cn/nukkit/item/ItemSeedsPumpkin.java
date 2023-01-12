/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemSeedsPumpkin
extends Item {
    public ItemSeedsPumpkin() {
        this((Integer)0, 1);
    }

    public ItemSeedsPumpkin(Integer n) {
        this(n, 1);
    }

    public ItemSeedsPumpkin(Integer n, int n2) {
        super(361, 0, n2, "Pumpkin Seeds");
        this.block = Block.get(104);
    }
}

