/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemDoorJungle
extends Item {
    public ItemDoorJungle() {
        this((Integer)0, 1);
    }

    public ItemDoorJungle(Integer n) {
        this(n, 1);
    }

    public ItemDoorJungle(Integer n, int n2) {
        super(429, 0, n2, "Jungle Door");
        this.block = Block.get(195);
    }
}

