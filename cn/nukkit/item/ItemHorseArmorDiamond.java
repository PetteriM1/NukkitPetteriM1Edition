/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemHorseArmorDiamond
extends Item {
    public ItemHorseArmorDiamond() {
        this((Integer)0, 0);
    }

    public ItemHorseArmorDiamond(Integer n) {
        this(n, 0);
    }

    public ItemHorseArmorDiamond(Integer n, int n2) {
        super(419, n, n2, "Diamond Horse Armor");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}

