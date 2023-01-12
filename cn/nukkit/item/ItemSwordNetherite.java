/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemSwordNetherite
extends ItemTool {
    public ItemSwordNetherite() {
        this((Integer)0, 1);
    }

    public ItemSwordNetherite(Integer n) {
        this(n, 1);
    }

    public ItemSwordNetherite(Integer n, int n2) {
        super(743, n, n2, "Netherite Sword");
    }

    @Override
    public boolean isSword() {
        return true;
    }

    @Override
    public int getAttackDamage() {
        return 8;
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

