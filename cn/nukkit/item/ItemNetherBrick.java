/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemNetherBrick
extends Item {
    public ItemNetherBrick() {
        this((Integer)0, 1);
    }

    public ItemNetherBrick(Integer n) {
        this(n, 1);
    }

    public ItemNetherBrick(Integer n, int n2) {
        super(405, n, n2, "Nether Brick");
    }
}

