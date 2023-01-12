/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemBootsDiamond
extends ItemArmor {
    public ItemBootsDiamond() {
        this((Integer)0, 1);
    }

    public ItemBootsDiamond(Integer n) {
        this(n, 1);
    }

    public ItemBootsDiamond(Integer n, int n2) {
        super(313, n, n2, "Diamond Boots");
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public boolean isBoots() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 3;
    }

    @Override
    public int getMaxDurability() {
        return 430;
    }

    @Override
    public int getToughness() {
        return 2;
    }
}

