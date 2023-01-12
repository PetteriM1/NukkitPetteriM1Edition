/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemHorseArmorIron
extends Item {
    public ItemHorseArmorIron() {
        this((Integer)0, 0);
    }

    public ItemHorseArmorIron(Integer n) {
        this(n, 0);
    }

    public ItemHorseArmorIron(Integer n, int n2) {
        super(417, n, n2, "Iron Horse Armor");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}

