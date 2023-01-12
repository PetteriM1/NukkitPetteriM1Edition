/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockCrops;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Utils;

public class BlockCarrot
extends BlockCrops {
    public BlockCarrot(int n) {
        super(n);
    }

    public BlockCarrot() {
        this(0);
    }

    @Override
    public String getName() {
        return "Carrot Block";
    }

    @Override
    public int getId() {
        return 141;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (this.getDamage() >= 7) {
            return new Item[]{Item.get(391, 0, Utils.rand(1, 5))};
        }
        return new Item[]{Item.get(391)};
    }

    @Override
    public Item toItem() {
        return Item.get(391);
    }
}

