/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemLeggingsChain
extends ItemArmor {
    public ItemLeggingsChain() {
        this((Integer)0, 1);
    }

    public ItemLeggingsChain(Integer n) {
        this(n, 1);
    }

    public ItemLeggingsChain(Integer n, int n2) {
        super(304, n, n2, "Chain Leggings");
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public boolean isLeggings() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 4;
    }

    @Override
    public int getMaxDurability() {
        return 226;
    }
}

