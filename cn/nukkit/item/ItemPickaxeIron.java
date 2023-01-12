/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemPickaxeIron
extends ItemTool {
    public ItemPickaxeIron() {
        this((Integer)0, 1);
    }

    public ItemPickaxeIron(Integer n) {
        this(n, 1);
    }

    public ItemPickaxeIron(Integer n, int n2) {
        super(257, n, n2, "Iron Pickaxe");
    }

    @Override
    public int getMaxDurability() {
        return 251;
    }

    @Override
    public boolean isPickaxe() {
        return true;
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public int getAttackDamage() {
        return 4;
    }
}

