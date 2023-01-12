/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecordChirp
extends ItemRecord {
    public ItemRecordChirp() {
        this((Integer)0, 1);
    }

    public ItemRecordChirp(Integer n) {
        this(n, 1);
    }

    public ItemRecordChirp(Integer n, int n2) {
        super(503, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.chirp";
    }

    @Override
    public String getDiscName() {
        return "C418 - chirp";
    }
}

