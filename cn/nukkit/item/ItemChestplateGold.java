/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemChestplateGold
extends ItemArmor {
    public ItemChestplateGold() {
        this((Integer)0, 1);
    }

    public ItemChestplateGold(Integer n) {
        this(n, 1);
    }

    public ItemChestplateGold(Integer n, int n2) {
        super(315, n, n2, "Golden Chestplate");
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public boolean isChestplate() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 5;
    }

    @Override
    public int getMaxDurability() {
        return 113;
    }
}

