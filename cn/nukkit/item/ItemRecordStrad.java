/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecordStrad
extends ItemRecord {
    public ItemRecordStrad() {
        this((Integer)0, 1);
    }

    public ItemRecordStrad(Integer n) {
        this(n, 1);
    }

    public ItemRecordStrad(Integer n, int n2) {
        super(508, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.strad";
    }

    @Override
    public String getDiscName() {
        return "C418 - strad";
    }
}

