/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;

public class BlockNetherReactor
extends BlockSolid {
    @Override
    public int getId() {
        return 247;
    }

    @Override
    public String getName() {
        return "Nether Reactor Core";
    }

    @Override
    public double getHardness() {
        return 3.0;
    }

    @Override
    public double getResistance() {
        return 15.0;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= 1) {
            return new Item[]{Item.get(264, 0, 3), Item.get(265, 0, 6)};
        }
        return new Item[0];
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

