/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemLeggingsGold
extends ItemArmor {
    public ItemLeggingsGold() {
        this((Integer)0, 1);
    }

    public ItemLeggingsGold(Integer n) {
        this(n, 1);
    }

    public ItemLeggingsGold(Integer n, int n2) {
        super(316, n, n2, "Golden Leggings");
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public boolean isLeggings() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 3;
    }

    @Override
    public int getMaxDurability() {
        return 106;
    }
}

