/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemHoeDiamond
extends ItemTool {
    public ItemHoeDiamond() {
        this((Integer)0, 1);
    }

    public ItemHoeDiamond(Integer n) {
        this(n, 1);
    }

    public ItemHoeDiamond(Integer n, int n2) {
        super(293, n, n2, "Diamond Hoe");
    }

    @Override
    public int getMaxDurability() {
        return 1562;
    }

    @Override
    public boolean isHoe() {
        return true;
    }

    @Override
    public int getTier() {
        return 5;
    }
}

