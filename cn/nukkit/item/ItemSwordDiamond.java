/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemSwordDiamond
extends ItemTool {
    public ItemSwordDiamond() {
        this((Integer)0, 1);
    }

    public ItemSwordDiamond(Integer n) {
        this(n, 1);
    }

    public ItemSwordDiamond(Integer n, int n2) {
        super(276, n, n2, "Diamond Sword");
    }

    @Override
    public int getMaxDurability() {
        return 1562;
    }

    @Override
    public boolean isSword() {
        return true;
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public int getAttackDamage() {
        return 7;
    }
}

