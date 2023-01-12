/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTrapdoor;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockTrapdoorBirch
extends BlockTrapdoor {
    public BlockTrapdoorBirch() {
        this(0);
    }

    public BlockTrapdoorBirch(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Birch Trapdoor";
    }

    @Override
    public int getId() {
        return 401;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }
}

