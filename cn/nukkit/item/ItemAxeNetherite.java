/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemAxeNetherite
extends ItemTool {
    public ItemAxeNetherite() {
        this((Integer)0, 1);
    }

    public ItemAxeNetherite(Integer n) {
        this(n, 1);
    }

    public ItemAxeNetherite(Integer n, int n2) {
        super(746, n, n2, "Netherite Axe");
    }

    @Override
    public boolean isAxe() {
        return true;
    }

    @Override
    public int getAttackDamage() {
        return 7;
    }

    @Override
    public int getTier() {
        return 6;
    }

    @Override
    public int getMaxDurability() {
        return 2032;
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 407;
    }
}

