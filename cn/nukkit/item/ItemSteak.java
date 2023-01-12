/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemEdible;

public class ItemSteak
extends ItemEdible {
    public ItemSteak() {
        this((Integer)0, 1);
    }

    public ItemSteak(Integer n) {
        this(n, 1);
    }

    public ItemSteak(Integer n, int n2) {
        super(364, n, n2, "Steak");
    }
}

