/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemIngotGold
extends Item {
    public ItemIngotGold() {
        this((Integer)0, 1);
    }

    public ItemIngotGold(Integer n) {
        this(n, 1);
    }

    public ItemIngotGold(Integer n, int n2) {
        super(266, 0, n2, "Gold Ingot");
    }
}

