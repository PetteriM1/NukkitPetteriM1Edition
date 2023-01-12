/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;

public class BlockMossStone
extends BlockSolid {
    @Override
    public String getName() {
        return "Mossy Cobblestone";
    }

    @Override
    public int getId() {
        return 48;
    }

    @Override
    public double getHardness() {
        return 2.0;
    }

    @Override
    public double getResistance() {
        return 10.0;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

