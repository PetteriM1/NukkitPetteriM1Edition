/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecordBlocks
extends ItemRecord {
    public ItemRecordBlocks() {
        this((Integer)0, 1);
    }

    public ItemRecordBlocks(Integer n) {
        this(n, 1);
    }

    public ItemRecordBlocks(Integer n, int n2) {
        super(502, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.blocks";
    }

    @Override
    public String getDiscName() {
        return "C418 - blocks";
    }
}

