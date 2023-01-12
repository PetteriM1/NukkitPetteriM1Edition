/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemHoeNetherite
extends ItemTool {
    public ItemHoeNetherite() {
        this((Integer)0, 1);
    }

    public ItemHoeNetherite(Integer n) {
        this(n, 1);
    }

    public ItemHoeNetherite(Integer n, int n2) {
        super(747, n, n2, "Netherite Hoe");
    }

    @Override
    public boolean isHoe() {
        return true;
    }

    @Override
    public int getTier() {
        return 6;
    }

    @Override
    public int getMaxDurability() {
        return 2032;
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 407;
    }
}

