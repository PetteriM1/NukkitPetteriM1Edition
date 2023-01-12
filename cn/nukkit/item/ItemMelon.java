/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemEdible;

public class ItemMelon
extends ItemEdible {
    public ItemMelon() {
        this((Integer)0, 1);
    }

    public ItemMelon(Integer n) {
        this(n, 1);
    }

    public ItemMelon(Integer n, int n2) {
        super(360, n, n2, "Melon");
    }
}

