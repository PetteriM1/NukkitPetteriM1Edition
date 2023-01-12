/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemAxeGold
extends ItemTool {
    public ItemAxeGold() {
        this((Integer)0, 1);
    }

    public ItemAxeGold(Integer n) {
        this(n, 1);
    }

    public ItemAxeGold(Integer n, int n2) {
        super(286, n, n2, "Golden Axe");
    }

    @Override
    public int getMaxDurability() {
        return 33;
    }

    @Override
    public boolean isAxe() {
        return true;
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public int getAttackDamage() {
        return 3;
    }
}

