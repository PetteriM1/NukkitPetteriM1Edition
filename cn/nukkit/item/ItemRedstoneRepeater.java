/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemRedstoneRepeater
extends Item {
    public ItemRedstoneRepeater() {
        this((Integer)0);
    }

    public ItemRedstoneRepeater(Integer n) {
        this((Integer)0, 1);
    }

    public ItemRedstoneRepeater(Integer n, int n2) {
        super(356, n, n2, "Redstone Repeater");
        this.block = Block.get(93);
    }
}

