/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemHoeWood
extends ItemTool {
    public ItemHoeWood() {
        this((Integer)0, 1);
    }

    public ItemHoeWood(Integer n) {
        this(n, 1);
    }

    public ItemHoeWood(Integer n, int n2) {
        super(290, n, n2, "Wooden Hoe");
    }

    @Override
    public int getMaxDurability() {
        return 60;
    }

    @Override
    public boolean isHoe() {
        return true;
    }

    @Override
    public int getTier() {
        return 1;
    }
}

