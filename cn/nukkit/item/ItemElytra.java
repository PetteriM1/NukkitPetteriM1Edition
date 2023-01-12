/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemElytra
extends ItemArmor {
    public ItemElytra() {
        this((Integer)0, 1);
    }

    public ItemElytra(Integer n) {
        this(n, 1);
    }

    public ItemElytra(Integer n, int n2) {
        super(444, n, n2, "Elytra");
    }

    @Override
    public int getMaxDurability() {
        return 431;
    }

    @Override
    public boolean isArmor() {
        return false;
    }

    @Override
    public boolean isChestplate() {
        return true;
    }
}

