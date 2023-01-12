/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemItemFrame
extends Item {
    public ItemItemFrame() {
        this((Integer)0, 1);
    }

    public ItemItemFrame(Integer n) {
        this(n, 1);
    }

    public ItemItemFrame(Integer n, int n2) {
        super(389, n, n2, "Item Frame");
        this.block = Block.get(199);
    }
}

