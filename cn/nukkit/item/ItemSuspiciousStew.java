/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemEdible;

public class ItemSuspiciousStew
extends ItemEdible {
    public ItemSuspiciousStew() {
        this((Integer)0, 1);
    }

    public ItemSuspiciousStew(Integer n) {
        this(n, 1);
    }

    public ItemSuspiciousStew(Integer n, int n2) {
        super(734, n, n2, "Suspicious Stew");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 388;
    }
}

