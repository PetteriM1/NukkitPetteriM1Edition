/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemNautilusShell
extends Item {
    public ItemNautilusShell() {
        this((Integer)0, 1);
    }

    public ItemNautilusShell(Integer n) {
        this(n, 1);
    }

    public ItemNautilusShell(Integer n, int n2) {
        super(465, n, n2, "Nautilus Shell");
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 261;
    }
}

