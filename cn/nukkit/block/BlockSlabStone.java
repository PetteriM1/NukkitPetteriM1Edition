/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSlab;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockSlabStone
extends BlockSlab {
    public static final int STONE = 0;
    public static final int SANDSTONE = 1;
    public static final int WOODEN = 2;
    public static final int COBBLESTONE = 3;
    public static final int BRICK = 4;
    public static final int STONE_BRICK = 5;
    public static final int QUARTZ = 6;
    public static final int NETHER_BRICK = 7;

    public BlockSlabStone() {
        this(0);
    }

    public BlockSlabStone(int n) {
        this(n, 43);
    }

    public BlockSlabStone(int n, int n2) {
        super(n, n2);
    }

    @Override
    public int getId() {
        return 44;
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Stone", "Sandstone", "Wooden", "Cobblestone", "Brick", "Stone Brick", "Quartz", "Nether Brick"};
        return ((this.getDamage() & 8) > 0 ? "Upper " : "") + stringArray[this.getDamage() & 7] + " Slab";
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, this.getDamage() & 7);
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public BlockColor getColor() {
        switch (this.getDamage() & 7) {
            case 7: {
                return BlockColor.NETHERRACK_BLOCK_COLOR;
            }
            default: {
                return BlockColor.STONE_BLOCK_COLOR;
            }
            case 1: {
                return BlockColor.SAND_BLOCK_COLOR;
            }
            case 2: {
                return BlockColor.WOOD_BLOCK_COLOR;
            }
            case 6: 
        }
        return BlockColor.QUARTZ_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

