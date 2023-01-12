/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public abstract class ItemRecord
extends Item {
    public ItemRecord(int n, Integer n2, int n3) {
        super(n, n2, n3, "Music Disc");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    public abstract String getSoundId();

    public String getDiscName() {
        return "Unknown";
    }
}

