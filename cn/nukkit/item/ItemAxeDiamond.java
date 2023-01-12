/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemAxeDiamond
extends ItemTool {
    public ItemAxeDiamond() {
        this((Integer)0, 1);
    }

    public ItemAxeDiamond(Integer n) {
        this(n, 1);
    }

    public ItemAxeDiamond(Integer n, int n2) {
        super(279, n, n2, "Diamond Axe");
    }

    @Override
    public int getMaxDurability() {
        return 1562;
    }

    @Override
    public boolean isAxe() {
        return true;
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public int getAttackDamage() {
        return 6;
    }
}

