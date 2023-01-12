/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemSign
extends Item {
    public ItemSign() {
        this((Integer)0, 1);
    }

    public ItemSign(Integer n) {
        this(n, 1);
    }

    public ItemSign(Integer n, int n2) {
        super(323, 0, n2, "Sign");
        this.block = Block.get(63);
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}

