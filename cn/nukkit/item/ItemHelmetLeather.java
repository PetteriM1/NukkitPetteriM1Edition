/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemColorArmor;

public class ItemHelmetLeather
extends ItemColorArmor {
    public ItemHelmetLeather() {
        this((Integer)0, 1);
    }

    public ItemHelmetLeather(Integer n) {
        this(n, 1);
    }

    public ItemHelmetLeather(Integer n, int n2) {
        super(298, n, n2, "Leather Cap");
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public boolean isHelmet() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 1;
    }

    @Override
    public int getMaxDurability() {
        return 56;
    }
}

