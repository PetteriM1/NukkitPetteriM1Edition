/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemEdible;

public class ItemPorkchopCooked
extends ItemEdible {
    public ItemPorkchopCooked() {
        this((Integer)0, 1);
    }

    public ItemPorkchopCooked(Integer n) {
        this(n, 1);
    }

    public ItemPorkchopCooked(Integer n, int n2) {
        super(320, n, n2, "Cooked Porkchop");
    }
}

