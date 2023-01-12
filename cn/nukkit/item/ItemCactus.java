/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemCactus
extends Item {
    public ItemCactus() {
        this((Integer)0, 1);
    }

    public ItemCactus(Integer n) {
        this(n, 1);
    }

    public ItemCactus(Integer n, int n2) {
        super(81, 0, n2, "Cactus");
    }
}

