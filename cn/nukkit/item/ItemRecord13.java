/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecord13
extends ItemRecord {
    public ItemRecord13() {
        this((Integer)0, 1);
    }

    public ItemRecord13(Integer n) {
        this(n, 1);
    }

    public ItemRecord13(Integer n, int n2) {
        super(500, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.13";
    }

    @Override
    public String getDiscName() {
        return "C418 - 13";
    }
}

