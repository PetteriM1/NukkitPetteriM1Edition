/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemBlazePowder
extends Item {
    public ItemBlazePowder() {
        this((Integer)0, 1);
    }

    public ItemBlazePowder(Integer n) {
        this(n, 1);
    }

    public ItemBlazePowder(Integer n, int n2) {
        super(377, 0, n2, "Blaze Powder");
    }
}

