/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemBook
extends Item {
    public ItemBook() {
        this((Integer)0, 1);
    }

    public ItemBook(Integer n) {
        this(n, 1);
    }

    public ItemBook(Integer n, int n2) {
        super(340, n, n2, "Book");
    }

    @Override
    public int getEnchantAbility() {
        return 1;
    }
}

