/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemShears
extends ItemTool {
    public ItemShears() {
        this((Integer)0, 1);
    }

    public ItemShears(Integer n) {
        this(n, 1);
    }

    public ItemShears(Integer n, int n2) {
        super(359, n, n2, "Shears");
    }

    @Override
    public int getMaxDurability() {
        return 239;
    }

    @Override
    public boolean isShears() {
        return true;
    }
}

