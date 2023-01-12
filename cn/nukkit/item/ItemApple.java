/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemEdible;

public class ItemApple
extends ItemEdible {
    public ItemApple() {
        this((Integer)0, 1);
    }

    public ItemApple(Integer n) {
        this(n, 1);
    }

    public ItemApple(Integer n, int n2) {
        super(260, 0, n2, "Apple");
    }
}

