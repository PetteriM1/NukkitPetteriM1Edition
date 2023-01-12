/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemPickaxeDiamond
extends ItemTool {
    public ItemPickaxeDiamond() {
        this((Integer)0, 1);
    }

    public ItemPickaxeDiamond(Integer n) {
        this(n, 1);
    }

    public ItemPickaxeDiamond(Integer n, int n2) {
        super(278, n, n2, "Diamond Pickaxe");
    }

    @Override
    public int getMaxDurability() {
        return 1562;
    }

    @Override
    public boolean isPickaxe() {
        return true;
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public int getAttackDamage() {
        return 5;
    }
}

