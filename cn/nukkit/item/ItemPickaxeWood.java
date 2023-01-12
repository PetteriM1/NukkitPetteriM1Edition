/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemPickaxeWood
extends ItemTool {
    public ItemPickaxeWood() {
        this((Integer)0, 1);
    }

    public ItemPickaxeWood(Integer n) {
        this(n, 1);
    }

    public ItemPickaxeWood(Integer n, int n2) {
        super(270, n, n2, "Wooden Pickaxe");
    }

    @Override
    public int getMaxDurability() {
        return 60;
    }

    @Override
    public boolean isPickaxe() {
        return true;
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public int getAttackDamage() {
        return 2;
    }
}

