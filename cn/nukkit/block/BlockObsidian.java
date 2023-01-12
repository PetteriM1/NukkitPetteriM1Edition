/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockObsidian
extends BlockSolid {
    @Override
    public String getName() {
        return "Obsidian";
    }

    @Override
    public int getId() {
        return 49;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public double getHardness() {
        return 50.0;
    }

    @Override
    public double getResistance() {
        return 6000.0;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= 5) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public boolean onBreak(Item item) {
        Block[] blockArray;
        for (Block block : blockArray = new Block[]{this.up(), this.down(), this.north(), this.south(), this.west(), this.east()}) {
            if (block == null || block.getId() != 90) continue;
            block.onBreak(item);
        }
        return super.onBreak(item);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.OBSIDIAN_BLOCK_COLOR;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

