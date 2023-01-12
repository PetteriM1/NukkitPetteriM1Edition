/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemSignBirch
extends Item {
    public ItemSignBirch() {
        this((Integer)0, 1);
    }

    public ItemSignBirch(Integer n) {
        this(n, 1);
    }

    public ItemSignBirch(Integer n, int n2) {
        super(473, 0, n2, "Sign");
        this.block = Block.get(441);
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}

