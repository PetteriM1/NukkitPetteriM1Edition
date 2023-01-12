/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemString
extends Item {
    public ItemString() {
        this((Integer)0, 1);
    }

    public ItemString(Integer n) {
        this(n, 1);
    }

    public ItemString(Integer n, int n2) {
        super(287, n, n2, "String");
        this.block = Block.get(132);
    }
}

