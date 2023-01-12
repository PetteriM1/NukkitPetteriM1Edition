/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemAxeIron
extends ItemTool {
    public ItemAxeIron() {
        this((Integer)0, 1);
    }

    public ItemAxeIron(Integer n) {
        this(n, 1);
    }

    public ItemAxeIron(Integer n, int n2) {
        super(258, n, n2, "Iron Axe");
    }

    @Override
    public int getMaxDurability() {
        return 251;
    }

    @Override
    public boolean isAxe() {
        return true;
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public int getAttackDamage() {
        return 5;
    }
}

