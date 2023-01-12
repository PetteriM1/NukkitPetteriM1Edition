/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemFeather
extends Item {
    public ItemFeather() {
        this((Integer)0, 1);
    }

    public ItemFeather(Integer n) {
        this(n, 1);
    }

    public ItemFeather(Integer n, int n2) {
        super(288, 0, n2, "Feather");
    }
}

