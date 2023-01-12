/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemEdible;

public class ItemBread
extends ItemEdible {
    public ItemBread() {
        this((Integer)0, 1);
    }

    public ItemBread(Integer n) {
        this(n, 1);
    }

    public ItemBread(Integer n, int n2) {
        super(297, n, n2, "Bread");
    }
}

