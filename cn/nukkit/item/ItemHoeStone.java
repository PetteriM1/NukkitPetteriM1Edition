/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemHoeStone
extends ItemTool {
    public ItemHoeStone() {
        this((Integer)0, 1);
    }

    public ItemHoeStone(Integer n) {
        this(n, 1);
    }

    public ItemHoeStone(Integer n, int n2) {
        super(291, n, n2, "Stone Hoe");
    }

    @Override
    public int getMaxDurability() {
        return 132;
    }

    @Override
    public boolean isHoe() {
        return true;
    }

    @Override
    public int getTier() {
        return 3;
    }
}

