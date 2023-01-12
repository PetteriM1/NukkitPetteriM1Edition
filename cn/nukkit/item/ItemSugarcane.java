/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemSugarcane
extends Item {
    public ItemSugarcane() {
        this((Integer)0, 1);
    }

    public ItemSugarcane(Integer n) {
        this(n, 1);
    }

    public ItemSugarcane(Integer n, int n2) {
        super(338, 0, n2, "Sugar Canes");
        this.block = Block.get(83);
    }
}

