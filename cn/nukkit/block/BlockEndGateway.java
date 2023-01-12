/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockEndGateway
extends BlockSolid {
    @Override
    public String getName() {
        return "End Gateway";
    }

    @Override
    public int getId() {
        return 209;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public double getHardness() {
        return -1.0;
    }

    @Override
    public double getResistance() {
        return 1.8E7;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.BLACK_BLOCK_COLOR;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(0));
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}

