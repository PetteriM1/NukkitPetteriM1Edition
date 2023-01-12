/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemLodestoneCompass
extends Item {
    public ItemLodestoneCompass() {
        this((Integer)0, 1);
    }

    public ItemLodestoneCompass(Integer n) {
        this(n, 1);
    }

    public ItemLodestoneCompass(Integer n, int n2) {
        super(741, n, n2, "Lodestone Compass");
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 407;
    }
}

