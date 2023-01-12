/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemNuggetGold
extends Item {
    public ItemNuggetGold() {
        this((Integer)0, 1);
    }

    public ItemNuggetGold(Integer n) {
        this(n, 1);
    }

    public ItemNuggetGold(Integer n, int n2) {
        super(371, n, n2, "Gold Nugget");
    }
}

