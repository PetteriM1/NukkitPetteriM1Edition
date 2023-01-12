/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemScute
extends Item {
    public ItemScute() {
        this((Integer)0, 1);
    }

    public ItemScute(Integer n) {
        this(n, 1);
    }

    public ItemScute(Integer n, int n2) {
        super(468, n, n2, "Scute");
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 274;
    }
}

