/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemChestplateChain
extends ItemArmor {
    public ItemChestplateChain() {
        this((Integer)0, 1);
    }

    public ItemChestplateChain(Integer n) {
        this(n, 1);
    }

    public ItemChestplateChain(Integer n, int n2) {
        super(303, n, n2, "Chain Chestplate");
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public boolean isChestplate() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 5;
    }

    @Override
    public int getMaxDurability() {
        return 241;
    }
}

