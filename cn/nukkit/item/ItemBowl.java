/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemBowl
extends Item {
    public ItemBowl() {
        this((Integer)0, 1);
    }

    public ItemBowl(Integer n) {
        this(n, 1);
    }

    public ItemBowl(Integer n, int n2) {
        super(281, 0, n2, "Bowl");
    }
}

