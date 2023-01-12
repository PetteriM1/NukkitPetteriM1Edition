/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemDoorDarkOak
extends Item {
    public ItemDoorDarkOak() {
        this((Integer)0, 1);
    }

    public ItemDoorDarkOak(Integer n) {
        this(n, 1);
    }

    public ItemDoorDarkOak(Integer n, int n2) {
        super(431, 0, n2, "Dark Oak Door");
        this.block = Block.get(197);
    }
}

