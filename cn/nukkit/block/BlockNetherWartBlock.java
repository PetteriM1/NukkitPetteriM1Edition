/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockNetherWartBlock
extends BlockSolid {
    @Override
    public String getName() {
        return "Nether Wart Block";
    }

    @Override
    public int getId() {
        return 214;
    }

    @Override
    public double getResistance() {
        return 1.0;
    }

    @Override
    public double getHardness() {
        return 1.0;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{this.toItem()};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.RED_BLOCK_COLOR;
    }

    @Override
    public int getToolType() {
        return 6;
    }
}

