/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockDoubleSlabWood
extends BlockSolidMeta {
    public BlockDoubleSlabWood() {
        this(0);
    }

    public BlockDoubleSlabWood(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 157;
    }

    @Override
    public double getHardness() {
        return 2.0;
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
    public String getName() {
        String[] stringArray = new String[]{"Oak", "Spruce", "Birch", "Jungle", "Acacia", "Dark Oak", "", ""};
        return "Double " + stringArray[this.getDamage() & 7] + " Slab";
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(158), this.getDamage() & 7);
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{new ItemBlock(Block.get(158), this.getDamage() & 7, 2)};
    }

    @Override
    public BlockColor getColor() {
        switch (this.getDamage() & 7) {
            default: {
                return BlockColor.WOOD_BLOCK_COLOR;
            }
            case 1: {
                return BlockColor.SPRUCE_BLOCK_COLOR;
            }
            case 2: {
                return BlockColor.SAND_BLOCK_COLOR;
            }
            case 3: {
                return BlockColor.DIRT_BLOCK_COLOR;
            }
            case 4: {
                return BlockColor.ORANGE_BLOCK_COLOR;
            }
            case 5: 
        }
        return BlockColor.BROWN_BLOCK_COLOR;
    }
}

