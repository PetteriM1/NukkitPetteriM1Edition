/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemSaddle
extends Item {
    public ItemSaddle() {
        this((Integer)0, 1);
    }

    public ItemSaddle(Integer n) {
        this(n, 1);
    }

    public ItemSaddle(Integer n, int n2) {
        super(329, n, n2, "Saddle");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}

