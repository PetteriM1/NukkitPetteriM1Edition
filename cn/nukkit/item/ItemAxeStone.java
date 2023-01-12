/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemAxeStone
extends ItemTool {
    public ItemAxeStone() {
        this((Integer)0, 1);
    }

    public ItemAxeStone(Integer n) {
        this(n, 1);
    }

    public ItemAxeStone(Integer n, int n2) {
        super(275, n, n2, "Stone Axe");
    }

    @Override
    public int getMaxDurability() {
        return 132;
    }

    @Override
    public boolean isAxe() {
        return true;
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public int getAttackDamage() {
        return 4;
    }
}

