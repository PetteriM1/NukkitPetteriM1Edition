/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemBrewingStand
extends Item {
    public ItemBrewingStand() {
        this((Integer)0, 1);
    }

    public ItemBrewingStand(Integer n) {
        this(n, 1);
    }

    public ItemBrewingStand(Integer n, int n2) {
        super(379, 0, n2, "Brewing Stand");
        this.block = Block.get(117);
    }
}

