/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemCarrotOnAStick
extends ItemTool {
    public ItemCarrotOnAStick() {
        this((Integer)0, 1);
    }

    public ItemCarrotOnAStick(Integer n) {
        this(n, 1);
    }

    public ItemCarrotOnAStick(Integer n, int n2) {
        super(398, n, n2, "Carrot on a stick");
    }

    @Override
    public int getMaxDurability() {
        return 25;
    }

    @Override
    public boolean noDamageOnAttack() {
        return true;
    }

    @Override
    public boolean noDamageOnBreak() {
        return true;
    }
}

