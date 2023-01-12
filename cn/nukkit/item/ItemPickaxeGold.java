/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemPickaxeGold
extends ItemTool {
    public ItemPickaxeGold() {
        this((Integer)0, 1);
    }

    public ItemPickaxeGold(Integer n) {
        this(n, 1);
    }

    public ItemPickaxeGold(Integer n, int n2) {
        super(285, n, n2, "Golden Pickaxe");
    }

    @Override
    public int getMaxDurability() {
        return 33;
    }

    @Override
    public boolean isPickaxe() {
        return true;
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public int getAttackDamage() {
        return 2;
    }
}

