/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemPickaxeStone
extends ItemTool {
    public ItemPickaxeStone() {
        this((Integer)0, 1);
    }

    public ItemPickaxeStone(Integer n) {
        this(n, 1);
    }

    public ItemPickaxeStone(Integer n, int n2) {
        super(274, n, n2, "Stone Pickaxe");
    }

    @Override
    public int getMaxDurability() {
        return 132;
    }

    @Override
    public boolean isPickaxe() {
        return true;
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public int getAttackDamage() {
        return 3;
    }
}

