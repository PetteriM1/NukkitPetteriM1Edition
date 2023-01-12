/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockCrops;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Utils;

public class BlockWheat
extends BlockCrops {
    public BlockWheat() {
        this(0);
    }

    public BlockWheat(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Wheat Block";
    }

    @Override
    public int getId() {
        return 59;
    }

    @Override
    public Item toItem() {
        return Item.get(295);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (this.getDamage() >= 7) {
            return new Item[]{Item.get(296), Item.get(295, 0, Utils.random.nextInt(1, 3))};
        }
        return new Item[]{Item.get(295)};
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

