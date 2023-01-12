/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.data;

import java.util.Objects;

public abstract class EntityData<T> {
    private int a;

    protected EntityData(int n) {
        this.a = n;
    }

    public abstract int getType();

    public abstract T getData();

    public abstract void setData(T var1);

    public int getId() {
        return this.a;
    }

    public EntityData setId(int n) {
        this.a = n;
        return this;
    }

    public boolean equals(Object object) {
        return object instanceof EntityData && ((EntityData)object).a == this.a && Objects.equals(((EntityData)object).getData(), this.getData());
    }
}

