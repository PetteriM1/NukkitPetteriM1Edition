/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemEdible;

public class ItemMushroomStew
extends ItemEdible {
    public ItemMushroomStew() {
        this((Integer)0, 1);
    }

    public ItemMushroomStew(Integer n) {
        this(n, 1);
    }

    public ItemMushroomStew(Integer n, int n2) {
        super(282, 0, n2, "Mushroom Stew");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}

