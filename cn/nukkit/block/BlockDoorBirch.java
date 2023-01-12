/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockDoorWood;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockDoorBirch
extends BlockDoorWood {
    public BlockDoorBirch() {
        this(0);
    }

    public BlockDoorBirch(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Birch Door Block";
    }

    @Override
    public int getId() {
        return 194;
    }

    @Override
    public Item toItem() {
        return Item.get(428);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }
}

