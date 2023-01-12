/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.data;

import cn.nukkit.entity.data.EntityData;

public class IntEntityData
extends EntityData<Integer> {
    public int data;

    public IntEntityData(int n, int n2) {
        super(n);
        this.data = n2;
    }

    @Override
    public Integer getData() {
        return this.data;
    }

    @Override
    public void setData(Integer n) {
        this.data = n == null ? 0 : n;
    }

    @Override
    public int getType() {
        return 2;
    }
}

