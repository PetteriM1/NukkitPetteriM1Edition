/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecordMellohi
extends ItemRecord {
    public ItemRecordMellohi() {
        this((Integer)0, 1);
    }

    public ItemRecordMellohi(Integer n) {
        this(n, 1);
    }

    public ItemRecordMellohi(Integer n, int n2) {
        super(506, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.mellohi";
    }

    @Override
    public String getDiscName() {
        return "C418 - mellohi";
    }
}

