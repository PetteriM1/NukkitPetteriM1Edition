/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecordFar
extends ItemRecord {
    public ItemRecordFar() {
        this((Integer)0, 1);
    }

    public ItemRecordFar(Integer n) {
        this(n, 1);
    }

    public ItemRecordFar(Integer n, int n2) {
        super(504, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.far";
    }

    @Override
    public String getDiscName() {
        return "C418 - far";
    }
}

