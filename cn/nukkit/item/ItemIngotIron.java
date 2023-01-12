/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemIngotIron
extends Item {
    public ItemIngotIron() {
        this((Integer)0, 1);
    }

    public ItemIngotIron(Integer n) {
        this(n, 1);
    }

    public ItemIngotIron(Integer n, int n2) {
        super(265, 0, n2, "Iron Ingot");
    }
}

