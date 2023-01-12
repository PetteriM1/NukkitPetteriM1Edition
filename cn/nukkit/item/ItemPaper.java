/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemPaper
extends Item {
    public ItemPaper() {
        this((Integer)0, 1);
    }

    public ItemPaper(Integer n) {
        this(n, 1);
    }

    public ItemPaper(Integer n, int n2) {
        super(339, n, n2, "Paper");
    }
}

