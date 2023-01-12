/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecordStal
extends ItemRecord {
    public ItemRecordStal() {
        this((Integer)0, 1);
    }

    public ItemRecordStal(Integer n) {
        this(n, 1);
    }

    public ItemRecordStal(Integer n, int n2) {
        super(507, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.stal";
    }

    @Override
    public String getDiscName() {
        return "C418 - stal";
    }
}

