/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemShovelDiamond
extends ItemTool {
    public ItemShovelDiamond() {
        this((Integer)0, 1);
    }

    public ItemShovelDiamond(Integer n) {
        this(n, 1);
    }

    public ItemShovelDiamond(Integer n, int n2) {
        super(277, n, n2, "Diamond Shovel");
    }

    @Override
    public int getMaxDurability() {
        return 1562;
    }

    @Override
    public boolean isShovel() {
        return true;
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public int getAttackDamage() {
        return 4;
    }
}

