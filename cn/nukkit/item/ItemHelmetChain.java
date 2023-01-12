/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemHelmetChain
extends ItemArmor {
    public ItemHelmetChain() {
        this((Integer)0, 1);
    }

    public ItemHelmetChain(Integer n) {
        this(n, 1);
    }

    public ItemHelmetChain(Integer n, int n2) {
        super(302, n, n2, "Chain Helmet");
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public boolean isHelmet() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 2;
    }

    @Override
    public int getMaxDurability() {
        return 166;
    }
}

