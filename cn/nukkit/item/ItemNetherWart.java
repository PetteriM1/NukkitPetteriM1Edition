/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemNetherWart
extends Item {
    public ItemNetherWart() {
        this((Integer)0, 1);
    }

    public ItemNetherWart(Integer n) {
        this(n, 1);
    }

    public ItemNetherWart(Integer n, int n2) {
        super(372, n, n2, "Nether Wart");
        this.block = Block.get(115, n);
    }
}

