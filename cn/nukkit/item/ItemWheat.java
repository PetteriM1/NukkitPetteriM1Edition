/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemWheat
extends Item {
    public ItemWheat() {
        this((Integer)0, 1);
    }

    public ItemWheat(Integer n) {
        this(n, 1);
    }

    public ItemWheat(Integer n, int n2) {
        super(296, n, n2, "Wheat");
    }
}

