/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemHoeGold
extends ItemTool {
    public ItemHoeGold() {
        this((Integer)0, 1);
    }

    public ItemHoeGold(Integer n) {
        this(n, 1);
    }

    public ItemHoeGold(Integer n, int n2) {
        super(294, n, n2, "Golden Hoe");
    }

    @Override
    public int getMaxDurability() {
        return 33;
    }

    @Override
    public boolean isHoe() {
        return true;
    }

    @Override
    public int getTier() {
        return 2;
    }
}

