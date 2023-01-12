/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemFish;

public class ItemSalmon
extends ItemFish {
    public ItemSalmon() {
        this((Integer)0, 1);
    }

    public ItemSalmon(Integer n) {
        this(n, 1);
    }

    public ItemSalmon(Integer n, int n2) {
        super(460, n, n2, "Raw Salmon");
    }
}

