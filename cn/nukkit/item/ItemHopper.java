/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemHopper
extends Item {
    public ItemHopper() {
        this((Integer)0);
    }

    public ItemHopper(Integer n) {
        this(n, 1);
    }

    public ItemHopper(Integer n, int n2) {
        super(410, 0, n2, "Hopper");
        this.block = Block.get(154);
    }
}

