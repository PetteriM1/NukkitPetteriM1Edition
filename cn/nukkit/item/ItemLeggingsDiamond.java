/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemLeggingsDiamond
extends ItemArmor {
    public ItemLeggingsDiamond() {
        this((Integer)0, 1);
    }

    public ItemLeggingsDiamond(Integer n) {
        this(n, 1);
    }

    public ItemLeggingsDiamond(Integer n, int n2) {
        super(312, n, n2, "Diamond Leggings");
    }

    @Override
    public boolean isLeggings() {
        return true;
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public int getArmorPoints() {
        return 6;
    }

    @Override
    public int getMaxDurability() {
        return 496;
    }

    @Override
    public int getToughness() {
        return 2;
    }
}

