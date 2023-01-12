/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemSwordIron
extends ItemTool {
    public ItemSwordIron() {
        this((Integer)0, 1);
    }

    public ItemSwordIron(Integer n) {
        this(n, 1);
    }

    public ItemSwordIron(Integer n, int n2) {
        super(267, n, n2, "Iron Sword");
    }

    @Override
    public int getMaxDurability() {
        return 251;
    }

    @Override
    public boolean isSword() {
        return true;
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public int getAttackDamage() {
        return 6;
    }
}

