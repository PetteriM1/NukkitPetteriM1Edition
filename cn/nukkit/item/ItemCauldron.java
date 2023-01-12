/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemCauldron
extends Item {
    public ItemCauldron() {
        this((Integer)0, 1);
    }

    public ItemCauldron(Integer n) {
        this(n, 1);
    }

    public ItemCauldron(Integer n, int n2) {
        super(380, n, n2, "Cauldron");
        this.block = Block.get(118);
    }
}

