/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.ItemEdible;

public class ItemSweetBerries
extends ItemEdible {
    public ItemSweetBerries() {
        this((Integer)0, 1);
    }

    public ItemSweetBerries(Integer n) {
        this(n, 1);
    }

    public ItemSweetBerries(Integer n, int n2) {
        super(477, n, n2, "Sweet Berries");
        this.block = Block.get(462);
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 354;
    }
}

