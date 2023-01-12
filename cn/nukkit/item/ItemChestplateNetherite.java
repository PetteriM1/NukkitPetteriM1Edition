/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemChestplateNetherite
extends ItemArmor {
    public ItemChestplateNetherite() {
        this((Integer)0, 1);
    }

    public ItemChestplateNetherite(Integer n) {
        this(n, 1);
    }

    public ItemChestplateNetherite(Integer n, int n2) {
        super(749, n, n2, "Netherite Chestplate");
    }

    @Override
    public boolean isChestplate() {
        return true;
    }

    @Override
    public int getTier() {
        return 6;
    }

    @Override
    public int getMaxDurability() {
        return 592;
    }

    @Override
    public int getArmorPoints() {
        return 8;
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

