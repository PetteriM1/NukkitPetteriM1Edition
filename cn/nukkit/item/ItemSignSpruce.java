/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemSignSpruce
extends Item {
    public ItemSignSpruce() {
        this((Integer)0, 1);
    }

    public ItemSignSpruce(Integer n) {
        this(n, 1);
    }

    public ItemSignSpruce(Integer n, int n2) {
        super(472, 0, n2, "Sign");
        this.block = Block.get(436);
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}

