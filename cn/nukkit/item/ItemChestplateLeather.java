/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemColorArmor;

public class ItemChestplateLeather
extends ItemColorArmor {
    public ItemChestplateLeather() {
        this((Integer)0, 1);
    }

    public ItemChestplateLeather(Integer n) {
        this(n, 1);
    }

    public ItemChestplateLeather(Integer n, int n2) {
        super(299, n, n2, "Leather Tunic");
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public boolean isChestplate() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 3;
    }

    @Override
    public int getMaxDurability() {
        return 81;
    }
}

