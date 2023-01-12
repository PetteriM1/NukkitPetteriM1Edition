/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecord11
extends ItemRecord {
    public ItemRecord11() {
        this((Integer)0, 1);
    }

    public ItemRecord11(Integer n) {
        this(n, 1);
    }

    public ItemRecord11(Integer n, int n2) {
        super(510, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.11";
    }

    @Override
    public String getDiscName() {
        return "C418 - 11";
    }
}

