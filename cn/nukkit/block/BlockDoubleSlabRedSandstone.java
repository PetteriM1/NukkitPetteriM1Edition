/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockDoubleSlabRedSandstone
extends BlockSolidMeta {
    public BlockDoubleSlabRedSandstone() {
        this(0);
    }

    public BlockDoubleSlabRedSandstone(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 181;
    }

    @Override
    public double getResistance() {
        return 30.0;
    }

    @Override
    public double getHardness() {
        return 2.0;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Red Sandstone", "Purpur", "", "", "", "", "", ""};
        return "Double " + stringArray[this.getDamage() & 7] + " Slab";
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(182), this.getDamage() & 7);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{Item.get(182, this.getDamage() & 7, 2)};
        }
        return new Item[0];
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockColor getColor() {
        switch (this.getDamage() & 7) {
            case 0: {
                return BlockColor.ORANGE_BLOCK_COLOR;
            }
            case 1: {
                return BlockColor.PURPLE_BLOCK_COLOR;
            }
        }
        return BlockColor.STONE_BLOCK_COLOR;
    }
}

