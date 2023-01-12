/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemGlowstoneDust
extends Item {
    public ItemGlowstoneDust() {
        this((Integer)0, 1);
    }

    public ItemGlowstoneDust(Integer n) {
        this(n, 1);
    }

    public ItemGlowstoneDust(Integer n, int n2) {
        super(348, n, n2, "Glowstone Dust");
    }
}

