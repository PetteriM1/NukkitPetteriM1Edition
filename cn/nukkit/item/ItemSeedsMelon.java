/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemSeedsMelon
extends Item {
    public ItemSeedsMelon() {
        this((Integer)0, 1);
    }

    public ItemSeedsMelon(Integer n) {
        this(n, 1);
    }

    public ItemSeedsMelon(Integer n, int n2) {
        super(362, 0, n2, "Melon Seeds");
        this.block = Block.get(105);
    }
}

