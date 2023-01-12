/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemSwordWood
extends ItemTool {
    public ItemSwordWood() {
        this((Integer)0, 1);
    }

    public ItemSwordWood(Integer n) {
        this(n, 1);
    }

    public ItemSwordWood(Integer n, int n2) {
        super(268, n, n2, "Wooden Sword");
    }

    @Override
    public int getMaxDurability() {
        return 60;
    }

    @Override
    public boolean isSword() {
        return true;
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public int getAttackDamage() {
        return 4;
    }
}

