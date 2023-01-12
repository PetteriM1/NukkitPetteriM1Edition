/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockBricksEndStone
extends BlockSolid {
    @Override
    public String getName() {
        return "End Stone Bricks";
    }

    @Override
    public int getId() {
        return 206;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public double getHardness() {
        return 0.8;
    }

    @Override
    public double getResistance() {
        return 4.0;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{Item.get(206, 0, 1)};
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }
}

