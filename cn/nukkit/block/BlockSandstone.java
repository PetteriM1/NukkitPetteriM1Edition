/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockSandstone
extends BlockSolidMeta {
    public static final int NORMAL = 0;
    public static final int CHISELED = 1;
    public static final int SMOOTH = 2;

    public BlockSandstone() {
        this(0);
    }

    public BlockSandstone(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 24;
    }

    @Override
    public double getHardness() {
        return 0.8;
    }

    @Override
    public double getResistance() {
        return 4.0;
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Sandstone", "Chiseled Sandstone", "Smooth Sandstone", ""};
        return stringArray[this.getDamage() & 3];
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
        return new ItemBlock((Block)this, this.getDamage() & 3);
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

