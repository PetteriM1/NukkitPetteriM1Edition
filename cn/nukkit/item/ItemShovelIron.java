/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemShovelIron
extends ItemTool {
    public ItemShovelIron() {
        this((Integer)0, 1);
    }

    public ItemShovelIron(Integer n) {
        this(n, 1);
    }

    public ItemShovelIron(Integer n, int n2) {
        super(256, n, n2, "Iron Shovel");
    }

    @Override
    public int getMaxDurability() {
        return 251;
    }

    @Override
    public boolean isShovel() {
        return true;
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public int getAttackDamage() {
        return 3;
    }
}

