/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockDoorWood;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockDoorDarkOak
extends BlockDoorWood {
    public BlockDoorDarkOak() {
        this(0);
    }

    public BlockDoorDarkOak(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Dark Oak Door Block";
    }

    @Override
    public int getId() {
        return 197;
    }

    @Override
    public Item toItem() {
        return Item.get(431);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.BROWN_BLOCK_COLOR;
    }
}

