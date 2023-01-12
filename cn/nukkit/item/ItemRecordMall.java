/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecordMall
extends ItemRecord {
    public ItemRecordMall() {
        this((Integer)0, 1);
    }

    public ItemRecordMall(Integer n) {
        this(n, 1);
    }

    public ItemRecordMall(Integer n, int n2) {
        super(505, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.mall";
    }

    @Override
    public String getDiscName() {
        return "C418 - mall";
    }
}

