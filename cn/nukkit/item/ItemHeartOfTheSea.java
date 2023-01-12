/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemHeartOfTheSea
extends Item {
    public ItemHeartOfTheSea() {
        this((Integer)0, 1);
    }

    public ItemHeartOfTheSea(Integer n) {
        this(n, 1);
    }

    public ItemHeartOfTheSea(Integer n, int n2) {
        super(467, n, n2, "Heart Of The Sea");
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 261;
    }
}

