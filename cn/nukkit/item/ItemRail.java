/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemRail
extends Item {
    public ItemRail() {
        this((Integer)0, 1);
    }

    public ItemRail(Integer n) {
        this(n, 1);
    }

    public ItemRail(Integer n, int n2) {
        super(66, 0, n2, "Rail");
    }
}

