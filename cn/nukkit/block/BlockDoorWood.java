/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockDoor;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockDoorWood
extends BlockDoor {
    public BlockDoorWood() {
        this(0);
    }

    public BlockDoorWood(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Wood Door Block";
    }

    @Override
    public int getId() {
        return 64;
    }

    @Override
    public double getHardness() {
        return 3.0;
    }

    @Override
    public double getResistance() {
        return 15.0;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public Item toItem() {
        return Item.get(324);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }
}

