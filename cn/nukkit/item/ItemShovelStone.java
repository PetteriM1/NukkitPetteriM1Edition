/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemShovelStone
extends ItemTool {
    public ItemShovelStone() {
        this((Integer)0, 1);
    }

    public ItemShovelStone(Integer n) {
        this(n, 1);
    }

    public ItemShovelStone(Integer n, int n2) {
        super(273, n, n2, "Stone Shovel");
    }

    @Override
    public int getMaxDurability() {
        return 132;
    }

    @Override
    public boolean isShovel() {
        return true;
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public int getAttackDamage() {
        return 2;
    }
}

