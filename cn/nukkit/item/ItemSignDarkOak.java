/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemSignDarkOak
extends Item {
    public ItemSignDarkOak() {
        this((Integer)0, 1);
    }

    public ItemSignDarkOak(Integer n) {
        this(n, 1);
    }

    public ItemSignDarkOak(Integer n, int n2) {
        super(476, 0, n2, "Sign");
        this.block = Block.get(447);
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}

