/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.data;

import cn.nukkit.entity.data.EntityData;

public class StringEntityData
extends EntityData<String> {
    public String data;

    public StringEntityData(int n, String string) {
        super(n);
        this.data = string;
    }

    @Override
    public String getData() {
        return this.data;
    }

    @Override
    public void setData(String string) {
        this.data = string;
    }

    @Override
    public int getType() {
        return 4;
    }

    public String toString() {
        return this.data;
    }
}

