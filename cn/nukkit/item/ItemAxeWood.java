/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemAxeWood
extends ItemTool {
    public ItemAxeWood() {
        this((Integer)0, 1);
    }

    public ItemAxeWood(Integer n) {
        this(n, 1);
    }

    public ItemAxeWood(Integer n, int n2) {
        super(271, n, n2, "Wooden Axe");
    }

    @Override
    public int getMaxDurability() {
        return 60;
    }

    @Override
    public boolean isAxe() {
        return true;
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public int getAttackDamage() {
        return 3;
    }
}

