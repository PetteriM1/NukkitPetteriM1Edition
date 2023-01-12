/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemFish;

public class ItemSalmonCooked
extends ItemFish {
    public ItemSalmonCooked() {
        this((Integer)0, 1);
    }

    public ItemSalmonCooked(Integer n) {
        this(n, 1);
    }

    public ItemSalmonCooked(Integer n, int n2) {
        super(463, n, n2, "Cooked Salmon");
    }
}

