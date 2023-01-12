/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemStick
extends Item {
    public ItemStick() {
        this((Integer)0, 1);
    }

    public ItemStick(Integer n) {
        this(n, 1);
    }

    public ItemStick(Integer n, int n2) {
        super(280, 0, n2, "Stick");
    }
}

