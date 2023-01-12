/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.tag;

import cn.nukkit.nbt.tag.Tag;

public abstract class NumberTag<T extends Number>
extends Tag {
    protected NumberTag(String string) {
        super(string);
    }

    public abstract T getData();

    public abstract void setData(T var1);
}

