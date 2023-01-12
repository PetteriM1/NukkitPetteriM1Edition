/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemBannerPattern
extends Item {
    public ItemBannerPattern() {
        this((Integer)0, 1);
    }

    public ItemBannerPattern(Integer n) {
        this(n, 1);
    }

    public ItemBannerPattern(Integer n, int n2) {
        super(434, n, n2, "Banner Pattern");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}

