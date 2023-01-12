/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemRecord;

public class ItemRecord5
extends ItemRecord {
    public ItemRecord5() {
        this((Integer)0, 1);
    }

    public ItemRecord5(Integer n) {
        this(n, 1);
    }

    public ItemRecord5(Integer n, int n2) {
        super(636, n, n2);
    }

    @Override
    public String getSoundId() {
        return "record.5";
    }

    @Override
    public String getDiscName() {
        return "Samuel \u00c5berg - 5";
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 524;
    }
}

