/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemHorseArmorLeather
extends Item {
    public ItemHorseArmorLeather() {
        this((Integer)0, 1);
    }

    public ItemHorseArmorLeather(Integer n) {
        this(n, 1);
    }

    public ItemHorseArmorLeather(Integer n, int n2) {
        super(416, n, n2, "Leather Horse Armor");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}

