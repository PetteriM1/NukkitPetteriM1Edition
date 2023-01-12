/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemClock
extends Item {
    public ItemClock() {
        this((Integer)0, 1);
    }

    public ItemClock(Integer n) {
        this(n, 1);
    }

    public ItemClock(Integer n, int n2) {
        super(347, n, n2, "Clock");
    }
}

