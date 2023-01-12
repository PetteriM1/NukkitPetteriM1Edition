/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemDiamond
extends Item {
    public ItemDiamond() {
        this((Integer)0, 1);
    }

    public ItemDiamond(Integer n) {
        this(n, 1);
    }

    public ItemDiamond(Integer n, int n2) {
        super(264, 0, n2, "Diamond");
    }
}

