/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemGunpowder
extends Item {
    public ItemGunpowder() {
        this((Integer)0, 1);
    }

    public ItemGunpowder(Integer n) {
        this(n, 1);
    }

    public ItemGunpowder(Integer n, int n2) {
        super(289, n, n2, "Gunpowder");
    }
}

