/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemSignJungle
extends Item {
    public ItemSignJungle() {
        this((Integer)0, 1);
    }

    public ItemSignJungle(Integer n) {
        this(n, 1);
    }

    public ItemSignJungle(Integer n, int n2) {
        super(474, 0, n2, "Sign");
        this.block = Block.get(443);
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}

