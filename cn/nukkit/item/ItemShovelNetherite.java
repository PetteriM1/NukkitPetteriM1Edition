/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemShovelNetherite
extends ItemTool {
    public ItemShovelNetherite() {
        this((Integer)0, 1);
    }

    public ItemShovelNetherite(Integer n) {
        this(n, 1);
    }

    public ItemShovelNetherite(Integer n, int n2) {
        super(744, n, n2, "Netherite Shovel");
    }

    @Override
    public boolean isShovel() {
        return true;
    }

    @Override
    public int getAttackDamage() {
        return 5;
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

