/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemDoorIron
extends Item {
    public ItemDoorIron() {
        this((Integer)0, 1);
    }

    public ItemDoorIron(Integer n) {
        this(n, 1);
    }

    public ItemDoorIron(Integer n, int n2) {
        super(330, 0, n2, "Iron Door");
        this.block = Block.get(71);
    }
}

