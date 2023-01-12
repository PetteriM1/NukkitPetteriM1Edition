/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSlab;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockSlabWood
extends BlockSlab {
    public BlockSlabWood() {
        this(0);
    }

    public BlockSlabWood(int n) {
        super(n, 157);
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Oak", "Spruce", "Birch", "Jungle", "Acacia", "Dark Oak", "", ""};
        return ((this.getDamage() & 8) == 8 ? "Upper " : "") + stringArray[this.getDamage() & 7] + " Wooden Slab";
    }

    @Override
    public int getId() {
        return 158;
    }

    @Override
    public int getBurnChance() {
        return 5;
    }

    @Override
    public int getBurnAbility() {
        return 20;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{this.toItem()};
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, this.getDamage() & 7);
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

