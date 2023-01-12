/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockNetherBrick;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockBricksRedNether
extends BlockNetherBrick {
    @Override
    public String getName() {
        return "Red Nether Bricks";
    }

    @Override
    public int getId() {
        return 215;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{Item.get(215, 0, 1)};
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.NETHERRACK_BLOCK_COLOR;
    }
}

