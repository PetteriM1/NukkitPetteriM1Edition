/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemHoneycomb
extends Item {
    public ItemHoneycomb() {
        this((Integer)0, 1);
    }

    public ItemHoneycomb(Integer n) {
        this(n, 1);
    }

    public ItemHoneycomb(Integer n, int n2) {
        super(736, n, n2, "Honeycomb");
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 389;
    }
}

