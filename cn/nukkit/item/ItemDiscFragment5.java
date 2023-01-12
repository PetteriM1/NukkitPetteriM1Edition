/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemDiscFragment5
extends Item {
    public ItemDiscFragment5() {
        this((Integer)0, 1);
    }

    public ItemDiscFragment5(Integer n) {
        this(n, 1);
    }

    public ItemDiscFragment5(Integer n, int n2) {
        super(637, n, n2, "Disc Fragment");
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 524;
    }
}

