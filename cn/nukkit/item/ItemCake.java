/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemCake
extends Item {
    public ItemCake() {
        this((Integer)0, 1);
    }

    public ItemCake(Integer n) {
        this(n, 1);
    }

    public ItemCake(Integer n, int n2) {
        super(354, 0, n2, "Cake");
        this.block = Block.get(92);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }
}

