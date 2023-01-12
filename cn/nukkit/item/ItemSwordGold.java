/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemSwordGold
extends ItemTool {
    public ItemSwordGold() {
        this((Integer)0, 1);
    }

    public ItemSwordGold(Integer n) {
        this(n, 1);
    }

    public ItemSwordGold(Integer n, int n2) {
        super(283, n, n2, "Golden Sword");
    }

    @Override
    public int getMaxDurability() {
        return 33;
    }

    @Override
    public boolean isSword() {
        return true;
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public int getAttackDamage() {
        return 4;
    }
}

