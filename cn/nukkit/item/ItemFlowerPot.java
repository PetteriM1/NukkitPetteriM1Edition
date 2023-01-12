/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemFlowerPot
extends Item {
    public ItemFlowerPot() {
        this((Integer)0, 1);
    }

    public ItemFlowerPot(Integer n) {
        this(n, 1);
    }

    public ItemFlowerPot(Integer n, int n2) {
        super(390, n, n2, "Flower Pot");
        this.block = Block.get(140);
    }
}

