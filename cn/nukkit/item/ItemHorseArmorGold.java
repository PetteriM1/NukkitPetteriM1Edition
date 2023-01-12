/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemHorseArmorGold
extends Item {
    public ItemHorseArmorGold() {
        this((Integer)0, 0);
    }

    public ItemHorseArmorGold(Integer n) {
        this(n, 0);
    }

    public ItemHorseArmorGold(Integer n, int n2) {
        super(418, n, n2, "Golden Horse Armor");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}

