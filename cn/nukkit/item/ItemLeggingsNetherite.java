/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemLeggingsNetherite
extends ItemArmor {
    public ItemLeggingsNetherite() {
        this((Integer)0, 1);
    }

    public ItemLeggingsNetherite(Integer n) {
        this(n, 1);
    }

    public ItemLeggingsNetherite(Integer n, int n2) {
        super(750, n, n2, "Netherite Leggings");
    }

    @Override
    public boolean isLeggings() {
        return true;
    }

    @Override
    public int getTier() {
        return 6;
    }

    @Override
    public int getMaxDurability() {
        return 555;
    }

    @Override
    public int getArmorPoints() {
        return 6;
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

