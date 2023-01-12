/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecordWard
extends ItemRecord {
    public ItemRecordWard() {
        this((Integer)0, 1);
    }

    public ItemRecordWard(Integer n) {
        this(n, 1);
    }

    public ItemRecordWard(Integer n, int n2) {
        super(509, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.ward";
    }

    @Override
    public String getDiscName() {
        return "C418 - ward";
    }
}

