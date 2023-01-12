/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemPickaxeNetherite
extends ItemTool {
    public ItemPickaxeNetherite() {
        this((Integer)0, 1);
    }

    public ItemPickaxeNetherite(Integer n) {
        this(n, 1);
    }

    public ItemPickaxeNetherite(Integer n, int n2) {
        super(745, n, n2, "Netherite Pickaxe");
    }

    @Override
    public boolean isPickaxe() {
        return true;
    }

    @Override
    public int getAttackDamage() {
        return 6;
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

