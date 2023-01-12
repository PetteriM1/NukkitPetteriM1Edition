/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDoubleSlabStone;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockDoubleSlabStone3
extends BlockDoubleSlabStone {
    public BlockDoubleSlabStone3() {
        this(0);
    }

    public BlockDoubleSlabStone3(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Double End Stone Brick Slab";
    }

    @Override
    public int getId() {
        return 422;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(417, this.getDamage()), this.getDamage() & 7);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{new ItemBlock(Block.get(417, this.getDamage()), this.getDamage() & 7, 2)};
        }
        return new Item[0];
    }
}

