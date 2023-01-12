/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemTool;

public class ItemWarpedFungusOnAStick
extends ItemTool {
    public ItemWarpedFungusOnAStick() {
        this((Integer)0, 1);
    }

    public ItemWarpedFungusOnAStick(Integer n) {
        this(n, 1);
    }

    public ItemWarpedFungusOnAStick(Integer n, int n2) {
        super(757, n, n2, "Warped Fungus on a Stick");
    }

    @Override
    public int getMaxDurability() {
        return 100;
    }

    @Override
    public boolean noDamageOnBreak() {
        return true;
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 407;
    }
}

