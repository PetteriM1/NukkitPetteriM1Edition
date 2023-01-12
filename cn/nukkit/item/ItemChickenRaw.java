/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemEdible;

public class ItemChickenRaw
extends ItemEdible {
    public ItemChickenRaw() {
        this((Integer)0, 1);
    }

    public ItemChickenRaw(Integer n) {
        this(n, 1);
    }

    public ItemChickenRaw(Integer n, int n2) {
        super(365, n, n2, "Raw Chicken");
    }
}

