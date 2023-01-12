/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemDoorWood
extends Item {
    public ItemDoorWood() {
        this((Integer)0, 1);
    }

    public ItemDoorWood(Integer n) {
        this(n, 1);
    }

    public ItemDoorWood(Integer n, int n2) {
        super(324, 0, n2, "Oak Door");
        this.block = Block.get(64);
    }
}

