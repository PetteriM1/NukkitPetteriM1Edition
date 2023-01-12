/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemLeggingsIron
extends ItemArmor {
    public ItemLeggingsIron() {
        this((Integer)0, 1);
    }

    public ItemLeggingsIron(Integer n) {
        this(n, 1);
    }

    public ItemLeggingsIron(Integer n, int n2) {
        super(308, n, n2, "Iron Leggings");
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public boolean isLeggings() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 5;
    }

    @Override
    public int getMaxDurability() {
        return 226;
    }
}

