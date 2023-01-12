/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemShovelGold
extends ItemTool {
    public ItemShovelGold() {
        this((Integer)0, 1);
    }

    public ItemShovelGold(Integer n) {
        this(n, 1);
    }

    public ItemShovelGold(Integer n, int n2) {
        super(284, n, n2, "Golden Shovel");
    }

    @Override
    public int getMaxDurability() {
        return 33;
    }

    @Override
    public boolean isShovel() {
        return true;
    }

    @Override
    public int getTier() {
        return 2;
    }
}

