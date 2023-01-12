/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecordCat
extends ItemRecord {
    public ItemRecordCat() {
        this((Integer)0, 1);
    }

    public ItemRecordCat(Integer n) {
        this(n, 1);
    }

    public ItemRecordCat(Integer n, int n2) {
        super(501, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.cat";
    }

    @Override
    public String getDiscName() {
        return "C418 - cat";
    }
}

