/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemRedstone
extends Item {
    public ItemRedstone() {
        this((Integer)0, 1);
    }

    public ItemRedstone(Integer n) {
        this(n, 1);
    }

    public ItemRedstone(Integer n, int n2) {
        super(331, n, n2, "Redstone Dust");
        this.block = Block.get(55);
    }
}

