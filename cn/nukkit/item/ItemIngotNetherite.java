/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemIngotNetherite
extends Item {
    public ItemIngotNetherite() {
        this((Integer)0, 1);
    }

    public ItemIngotNetherite(Integer n) {
        this(n, 1);
    }

    public ItemIngotNetherite(Integer n, int n2) {
        super(742, 0, n2, "Netherite Ingot");
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 407;
    }
}

