/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.data;

import cn.nukkit.entity.data.EntityData;

public class FloatEntityData
extends EntityData<Float> {
    public float data;

    public FloatEntityData(int n, float f2) {
        super(n);
        this.data = f2;
    }

    @Override
    public Float getData() {
        return Float.valueOf(this.data);
    }

    @Override
    public void setData(Float f2) {
        this.data = f2 == null ? 0.0f : f2.floatValue();
    }

    @Override
    public int getType() {
        return 3;
    }
}

