/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemSpyglass
extends Item {
    public ItemSpyglass() {
        this((Integer)0, 1);
    }

    public ItemSpyglass(Integer n) {
        this(n, 1);
    }

    public ItemSpyglass(Integer n, int n2) {
        super(772, 0, n2, "Spyglass");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 440;
    }
}

