/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecordPigstep
extends ItemRecord {
    public ItemRecordPigstep() {
        this((Integer)0, 1);
    }

    public ItemRecordPigstep(Integer n) {
        this(n, 1);
    }

    public ItemRecordPigstep(Integer n, int n2) {
        super(759, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.pigstep";
    }

    @Override
    public String getDiscName() {
        return "Lena Raine - Pigstep";
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 407;
    }
}

