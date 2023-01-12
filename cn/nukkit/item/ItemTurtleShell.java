/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;

public class ItemTurtleShell
extends ItemArmor {
    public ItemTurtleShell() {
        this((Integer)0, 1);
    }

    public ItemTurtleShell(Integer n) {
        this(n, 1);
    }

    public ItemTurtleShell(Integer n, int n2) {
        super(469, n, n2, "Turtle Shell");
    }

    @Override
    public int getTier() {
        return 7;
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
        return 276;
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 274;
    }
}

