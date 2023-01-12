/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDoubleSlabStone;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;

public class BlockDoubleSlabStone4
extends BlockDoubleSlabStone {
    public BlockDoubleSlabStone4() {
        this(0);
    }

    public BlockDoubleSlabStone4(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Double Mossy Stone Brick Slab";
    }

    @Override
    public int getId() {
        return 423;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(421, this.getDamage()), this.getDamage() & 7);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{new ItemBlock(Block.get(421, this.getDamage()), this.getDamage() & 7, 2)};
        }
        return new Item[0];
    }
}

