/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemTotem
extends Item {
    public ItemTotem(Integer n) {
        this(n, 1);
    }

    public ItemTotem(Integer n, int n2) {
        super(450, n, n2, "Totem of Undying");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}

