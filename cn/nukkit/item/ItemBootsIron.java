/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemBootsIron
extends ItemArmor {
    public ItemBootsIron() {
        this((Integer)0, 1);
    }

    public ItemBootsIron(Integer n) {
        this(n, 1);
    }

    public ItemBootsIron(Integer n, int n2) {
        super(309, n, n2, "Iron Boots");
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public boolean isBoots() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 2;
    }

    @Override
    public int getMaxDurability() {
        return 196;
    }
}

