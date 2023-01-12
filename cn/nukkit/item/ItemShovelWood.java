/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemShovelWood
extends ItemTool {
    public ItemShovelWood() {
        this((Integer)0, 1);
    }

    public ItemShovelWood(Integer n) {
        this(n, 1);
    }

    public ItemShovelWood(Integer n, int n2) {
        super(269, n, n2, "Wooden Shovel");
    }

    @Override
    public int getMaxDurability() {
        return 60;
    }

    @Override
    public boolean isShovel() {
        return true;
    }

    @Override
    public int getTier() {
        return 1;
    }
}

