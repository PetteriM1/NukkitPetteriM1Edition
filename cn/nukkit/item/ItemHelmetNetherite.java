/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemHelmetNetherite
extends ItemArmor {
    public ItemHelmetNetherite() {
        this((Integer)0, 1);
    }

    public ItemHelmetNetherite(Integer n) {
        this(n, 1);
    }

    public ItemHelmetNetherite(Integer n, int n2) {
        super(748, n, n2, "Netherite Helmet");
    }

    @Override
    public boolean isHelmet() {
        return true;
    }

    @Override
    public int getTier() {
        return 6;
    }

    @Override
    public int getMaxDurability() {
        return 407;
    }

    @Override
    public int getArmorPoints() {
        return 3;
    }

    @Override
    public int getToughness() {
        return 3;
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 407;
    }
}

