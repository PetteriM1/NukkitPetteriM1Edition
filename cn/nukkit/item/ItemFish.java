/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemEdible;

public class ItemFish
extends ItemEdible {
    public ItemFish() {
        this((Integer)0, 1);
    }

    public ItemFish(Integer n) {
        this(n, 1);
    }

    public ItemFish(Integer n, int n2) {
        super(349, n, n2, "Raw Fish");
    }

    protected ItemFish(int n, Integer n2, int n3, String string) {
        super(n, n2, n3, string);
    }
}

