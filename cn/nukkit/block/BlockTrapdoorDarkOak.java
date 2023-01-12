/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTrapdoor;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockTrapdoorDarkOak
extends BlockTrapdoor {
    public BlockTrapdoorDarkOak() {
        this(0);
    }

    public BlockTrapdoorDarkOak(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Dark Oak Trapdoor";
    }

    @Override
    public int getId() {
        return 402;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.BROWN_BLOCK_COLOR;
    }
}

