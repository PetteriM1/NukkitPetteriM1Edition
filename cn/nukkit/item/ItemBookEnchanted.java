/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemBookEnchanted
extends Item {
    public ItemBookEnchanted() {
        this((Integer)0, 1);
    }

    public ItemBookEnchanted(Integer n) {
        this(n, 1);
    }

    public ItemBookEnchanted(Integer n, int n2) {
        super(403, n, n2, "Enchanted Book");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}

