/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockCrops;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Utils;

public class BlockPotato
extends BlockCrops {
    public BlockPotato(int n) {
        super(n);
    }

    public BlockPotato() {
        this(0);
    }

    @Override
    public String getName() {
        return "Potato Block";
    }

    @Override
    public int getId() {
        return 142;
    }

    @Override
    public Item toItem() {
        return Item.get(392);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (this.getDamage() >= 7) {
            return new Item[]{Item.get(392, 0, Utils.random.nextInt(3) + 2)};
        }
        return new Item[]{Item.get(392)};
    }
}

