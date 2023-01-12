/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockCrops;
import cn.nukkit.item.Item;

public class BlockBeetroot
extends BlockCrops {
    public BlockBeetroot() {
        this(0);
    }

    public BlockBeetroot(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 244;
    }

    @Override
    public String getName() {
        return "Beetroot Block";
    }

    @Override
    public Item toItem() {
        return Item.get(458);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (this.getDamage() >= 7) {
            return new Item[]{Item.get(457, 0, 1), Item.get(458, 0, (int)(4.0 * Math.random()))};
        }
        return new Item[]{Item.get(458, 0, 1)};
    }
}

