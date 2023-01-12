/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemSwordStone
extends ItemTool {
    public ItemSwordStone() {
        this((Integer)0, 1);
    }

    public ItemSwordStone(Integer n) {
        this(n, 1);
    }

    public ItemSwordStone(Integer n, int n2) {
        super(272, n, n2, "Stone Sword");
    }

    @Override
    public int getMaxDurability() {
        return 132;
    }

    @Override
    public boolean isSword() {
        return true;
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public int getAttackDamage() {
        return 5;
    }
}

