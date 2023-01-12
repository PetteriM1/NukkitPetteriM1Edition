/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockClay
extends BlockSolid {
    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public double getResistance() {
        return 3.0;
    }

    @Override
    public int getToolType() {
        return 2;
    }

    @Override
    public int getId() {
        return 82;
    }

    @Override
    public String getName() {
        return "Clay Block";
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.hasEnchantment(16)) {
            return new Item[]{this.toItem()};
        }
        return new Item[]{Item.get(337, 0, 4)};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CLAY_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}

