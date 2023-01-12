/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSlabStone;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockSlabStone3
extends BlockSlabStone {
    public BlockSlabStone3() {
        this(0);
    }

    public BlockSlabStone3(int n) {
        super(n, 422);
    }

    @Override
    public String getName() {
        return "End Stone Brick Slab";
    }

    @Override
    public int getId() {
        return 417;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, this.getDamage() & 7);
    }
}

