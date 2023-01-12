/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemPhantomMembrane
extends Item {
    public ItemPhantomMembrane() {
        this((Integer)0, 1);
    }

    public ItemPhantomMembrane(Integer n) {
        this(n, 1);
    }

    public ItemPhantomMembrane(Integer n, int n2) {
        super(470, n, n2, "Phantom Membrane");
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 282;
    }
}

