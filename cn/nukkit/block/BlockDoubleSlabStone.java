/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockDoubleSlabStone
extends BlockSolidMeta {
    public static final int STONE = 0;
    public static final int SANDSTONE = 1;
    public static final int WOODEN = 2;
    public static final int COBBLESTONE = 3;
    public static final int BRICK = 4;
    public static final int STONE_BRICK = 5;
    public static final int QUARTZ = 6;
    public static final int NETHER_BRICK = 7;

    public BlockDoubleSlabStone() {
        this(0);
    }

    public BlockDoubleSlabStone(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 43;
    }

    @Override
    public double getResistance() {
        return this.getToolType() > 1 ? 30.0 : 15.0;
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
        String[] stringArray = new String[]{"Stone", "Sandstone", "Wooden", "Cobblestone", "Brick", "Stone Brick", "Quartz", "Nether Brick"};
        return "Double " + stringArray[this.getDamage() & 7] + " Slab";
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(44), this.getDamage() & 7);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{new ItemBlock(Block.get(44), this.getDamage() & 7, 2)};
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        switch (this.getDamage() & 7) {
            default: {
                return BlockColor.STONE_BLOCK_COLOR;
            }
            case 1: {
                return BlockColor.SAND_BLOCK_COLOR;
            }
            case 2: {
                return BlockColor.WOOD_BLOCK_COLOR;
            }
            case 6: {
                return BlockColor.QUARTZ_BLOCK_COLOR;
            }
            case 7: 
        }
        return BlockColor.NETHERRACK_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

