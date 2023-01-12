/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemDoorBirch
extends Item {
    public ItemDoorBirch() {
        this((Integer)0, 1);
    }

    public ItemDoorBirch(Integer n) {
        this(n, 1);
    }

    public ItemDoorBirch(Integer n, int n2) {
        super(428, 0, n2, "Birch Door");
        this.block = Block.get(194);
    }
}

