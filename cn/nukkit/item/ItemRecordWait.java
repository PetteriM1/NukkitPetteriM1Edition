/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecordWait
extends ItemRecord {
    public ItemRecordWait() {
        this((Integer)0, 1);
    }

    public ItemRecordWait(Integer n) {
        this(n, 1);
    }

    public ItemRecordWait(Integer n, int n2) {
        super(511, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.wait";
    }

    @Override
    public String getDiscName() {
        return "C418 - wait";
    }
}

