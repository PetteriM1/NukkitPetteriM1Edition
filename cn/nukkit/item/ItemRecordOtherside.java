/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecordOtherside
extends ItemRecord {
    public ItemRecordOtherside() {
        this((Integer)0, 1);
    }

    public ItemRecordOtherside(Integer n) {
        this(n, 1);
    }

    public ItemRecordOtherside(Integer n, int n2) {
        super(773, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.otherside";
    }

    @Override
    public String getDiscName() {
        return "Lena Raine - otherside";
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 476;
    }
}

