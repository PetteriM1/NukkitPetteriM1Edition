/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockDoorWood;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockDoorSpruce
extends BlockDoorWood {
    public BlockDoorSpruce() {
        this(0);
    }

    public BlockDoorSpruce(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Spruce Door Block";
    }

    @Override
    public int getId() {
        return 193;
    }

    @Override
    public Item toItem() {
        return Item.get(427);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SPRUCE_BLOCK_COLOR;
    }
}

