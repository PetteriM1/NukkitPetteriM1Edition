/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemShulkerShell
extends Item {
    public ItemShulkerShell() {
        this((Integer)0, 1);
    }

    public ItemShulkerShell(Integer n) {
        this(n, 1);
    }

    public ItemShulkerShell(Integer n, int n2) {
        super(445, 0, n2, "Shulker Shell");
    }
}

