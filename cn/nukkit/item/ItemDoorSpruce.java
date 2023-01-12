/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemDoorSpruce
extends Item {
    public ItemDoorSpruce() {
        this((Integer)0, 1);
    }

    public ItemDoorSpruce(Integer n) {
        this(n, 1);
    }

    public ItemDoorSpruce(Integer n, int n2) {
        super(427, 0, n2, "Spruce Door");
        this.block = Block.get(193);
    }
}

