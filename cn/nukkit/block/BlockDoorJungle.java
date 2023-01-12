/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockDoorWood;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockDoorJungle
extends BlockDoorWood {
    public BlockDoorJungle() {
        this(0);
    }

    public BlockDoorJungle(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Jungle Door Block";
    }

    @Override
    public int getId() {
        return 195;
    }

    @Override
    public Item toItem() {
        return Item.get(429);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }
}

