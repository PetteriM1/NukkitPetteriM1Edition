/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSlab;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockSlabRedSandstone
extends BlockSlab {
    public static final int RED_SANDSTONE = 0;
    public static final int PURPUR = 1;

    public BlockSlabRedSandstone() {
        this(0);
    }

    public BlockSlabRedSandstone(int n) {
        super(n, 181);
    }

    @Override
    public int getId() {
        return 182;
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Red Sandstone", "Purpur", "", "", "", "", "", ""};
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
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockColor getColor() {
        int n = this.getDamage() & 7;
        switch (n) {
            case 0: {
                return BlockColor.ORANGE_BLOCK_COLOR;
            }
            case 1: {
                return BlockColor.PURPLE_BLOCK_COLOR;
            }
            case 2: {
                return BlockColor.CYAN_BLOCK_COLOR;
            }
            case 3: {
                return BlockColor.DIAMOND_BLOCK_COLOR;
            }
            case 4: {
                return BlockColor.CYAN_BLOCK_COLOR;
            }
            case 5: {
                return BlockColor.STONE_BLOCK_COLOR;
            }
            case 6: {
                return BlockColor.SAND_BLOCK_COLOR;
            }
            case 7: {
                return BlockColor.NETHERRACK_BLOCK_COLOR;
            }
        }
        return BlockColor.STONE_BLOCK_COLOR;
    }
}

