/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemColorArmor;

public class ItemBootsLeather
extends ItemColorArmor {
    public ItemBootsLeather() {
        this((Integer)0, 1);
    }

    public ItemBootsLeather(Integer n) {
        this(n, 1);
    }

    public ItemBootsLeather(Integer n, int n2) {
        super(301, n, n2, "Leather Boots");
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public boolean isBoots() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 1;
    }

    @Override
    public int getMaxDurability() {
        return 66;
    }
}

