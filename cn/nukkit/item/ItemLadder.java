/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemLadder
extends Item {
    public ItemLadder() {
        this((Integer)0, 1);
    }

    public ItemLadder(Integer n) {
        this(n, 1);
    }

    public ItemLadder(Integer n, int n2) {
        super(65, 0, n2, "Ladder");
    }
}

