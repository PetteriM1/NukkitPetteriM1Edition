/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemHoeIron
extends ItemTool {
    public ItemHoeIron() {
        this((Integer)0, 1);
    }

    public ItemHoeIron(Integer n) {
        this(n, 1);
    }

    public ItemHoeIron(Integer n, int n2) {
        super(292, n, n2, "Iron Hoe");
    }

    @Override
    public int getMaxDurability() {
        return 251;
    }

    @Override
    public boolean isHoe() {
        return true;
    }

    @Override
    public int getTier() {
        return 4;
    }
}

