/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemChestplateIron
extends ItemArmor {
    public ItemChestplateIron() {
        this((Integer)0, 1);
    }

    public ItemChestplateIron(Integer n) {
        this(n, 1);
    }

    public ItemChestplateIron(Integer n, int n2) {
        super(307, n, n2, "Iron Chestplate");
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public boolean isChestplate() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 6;
    }

    @Override
    public int getMaxDurability() {
        return 241;
    }
}

