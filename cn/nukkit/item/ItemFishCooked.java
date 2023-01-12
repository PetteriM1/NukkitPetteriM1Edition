/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemFish;

public class ItemFishCooked
extends ItemFish {
    public ItemFishCooked() {
        this((Integer)0, 1);
    }

    public ItemFishCooked(Integer n) {
        this(n, 1);
    }

    public ItemFishCooked(Integer n, int n2) {
        super(350, n, n2, "Cooked Fish");
    }
}

