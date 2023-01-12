/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemEdible;

public class ItemChickenCooked
extends ItemEdible {
    public ItemChickenCooked() {
        this((Integer)0, 1);
    }

    public ItemChickenCooked(Integer n) {
        this(n, 1);
    }

    public ItemChickenCooked(Integer n, int n2) {
        super(366, n, n2, "Cooked Chicken");
    }
}

