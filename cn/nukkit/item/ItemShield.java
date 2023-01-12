/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemShield
extends ItemTool {
    public ItemShield() {
        this((Integer)0, 1);
    }

    public ItemShield(Integer n) {
        this(n, 1);
    }

    public ItemShield(Integer n, int n2) {
        super(513, n, n2, "Shield");
    }

    @Override
    public int getMaxDurability() {
        return 337;
    }
}

