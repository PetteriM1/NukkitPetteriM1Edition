/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemBootsGold
extends ItemArmor {
    public ItemBootsGold() {
        this((Integer)0, 1);
    }

    public ItemBootsGold(Integer n) {
        this(n, 1);
    }

    public ItemBootsGold(Integer n, int n2) {
        super(317, n, n2, "Golden Boots");
    }

    @Override
    public int getTier() {
        return 4;
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
        return 92;
    }
}

