/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemHelmetGold
extends ItemArmor {
    public ItemHelmetGold() {
        this((Integer)0, 1);
    }

    public ItemHelmetGold(Integer n) {
        this(n, 1);
    }

    public ItemHelmetGold(Integer n, int n2) {
        super(314, n, n2, "Golden Helmet");
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public boolean isHelmet() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 2;
    }

    @Override
    public int getMaxDurability() {
        return 78;
    }
}

