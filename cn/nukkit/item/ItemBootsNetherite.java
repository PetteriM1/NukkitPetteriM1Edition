/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemBootsNetherite
extends ItemArmor {
    public ItemBootsNetherite() {
        this((Integer)0, 1);
    }

    public ItemBootsNetherite(Integer n) {
        this(n, 1);
    }

    public ItemBootsNetherite(Integer n, int n2) {
        super(751, n, n2, "Netherite Boots");
    }

    @Override
    public boolean isBoots() {
        return true;
    }

    @Override
    public int getTier() {
        return 6;
    }

    @Override
    public int getMaxDurability() {
        return 481;
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

