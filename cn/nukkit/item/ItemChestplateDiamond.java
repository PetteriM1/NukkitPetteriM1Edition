/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemChestplateDiamond
extends ItemArmor {
    public ItemChestplateDiamond() {
        this((Integer)0, 1);
    }

    public ItemChestplateDiamond(Integer n) {
        this(n, 1);
    }

    public ItemChestplateDiamond(Integer n, int n2) {
        super(311, n, n2, "Diamond Chestplate");
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public boolean isChestplate() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 8;
    }

    @Override
    public int getMaxDurability() {
        return 529;
    }

    @Override
    public int getToughness() {
        return 2;
    }
}

