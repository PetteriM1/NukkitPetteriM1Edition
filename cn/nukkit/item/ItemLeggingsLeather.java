/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemColorArmor;

public class ItemLeggingsLeather
extends ItemColorArmor {
    public ItemLeggingsLeather() {
        this((Integer)0, 1);
    }

    public ItemLeggingsLeather(Integer n) {
        this(n, 1);
    }

    public ItemLeggingsLeather(Integer n, int n2) {
        super(300, n, n2, "Leather Pants");
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public boolean isLeggings() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 2;
    }

    @Override
    public int getMaxDurability() {
        return 76;
    }
}

