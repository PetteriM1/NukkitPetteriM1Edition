/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockObsidianGlowing
extends BlockSolid {
    @Override
    public int getId() {
        return 246;
    }

    @Override
    public String getName() {
        return "Glowing Obsidian";
    }

    @Override
    public int getLightLevel() {
        return 12;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(246));
    }

    @Override
    public boolean onBreak(Item item) {
        return this.getLevel().setBlock(this, Block.get(0), true, true);
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public double getHardness() {
        return 12.0;
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

