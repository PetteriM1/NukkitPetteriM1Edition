/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemBootsChain
extends ItemArmor {
    public ItemBootsChain() {
        this((Integer)0, 1);
    }

    public ItemBootsChain(Integer n) {
        this(n, 1);
    }

    public ItemBootsChain(Integer n, int n2) {
        super(305, n, n2, "Chainmail Boots");
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public boolean isBoots() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 1;
    }

    @Override
    public int getMaxDurability() {
        return 196;
    }
}

