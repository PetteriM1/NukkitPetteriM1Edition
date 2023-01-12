/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.data;

import cn.nukkit.entity.data.EntityData;

public class LongEntityData
extends EntityData<Long> {
    public long data;
    public long[] dataVersions;

    public LongEntityData(int n, long l) {
        super(n);
        this.data = l;
    }

    @Override
    public Long getData() {
        return this.data;
    }

    @Override
    public void setData(Long l) {
        this.data = l;
    }

    @Override
    public int getType() {
        return 7;
    }
}

