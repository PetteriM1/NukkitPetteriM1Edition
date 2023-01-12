/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemHelmetIron
extends ItemArmor {
    public ItemHelmetIron() {
        this((Integer)0, 1);
    }

    public ItemHelmetIron(Integer n) {
        this(n, 1);
    }

    public ItemHelmetIron(Integer n, int n2) {
        super(306, n, n2, "Iron Helmet");
    }

    @Override
    public int getTier() {
        return 2;
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
        return 166;
    }
}

