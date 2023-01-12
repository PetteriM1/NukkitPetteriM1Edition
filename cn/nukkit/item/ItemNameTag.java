/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemNameTag
extends Item {
    public ItemNameTag() {
        this((Integer)0, 1);
    }

    public ItemNameTag(Integer n) {
        this(n, 1);
    }

    public ItemNameTag(Integer n, int n2) {
        super(421, 0, n2, "Name Tag");
    }
}

