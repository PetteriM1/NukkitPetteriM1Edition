/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemHelmetDiamond
extends ItemArmor {
    public ItemHelmetDiamond() {
        this((Integer)0, 1);
    }

    public ItemHelmetDiamond(Integer n) {
        this(n, 1);
    }

    public ItemHelmetDiamond(Integer n, int n2) {
        super(310, n, n2, "Diamond Helmet");
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public boolean isHelmet() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 3;
    }

    @Override
    public int getMaxDurability() {
        return 364;
    }

    @Override
    public int getToughness() {
        return 2;
    }
}

