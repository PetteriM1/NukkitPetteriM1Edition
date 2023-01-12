/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockStone
extends BlockSolidMeta {
    public static final int NORMAL = 0;
    public static final int GRANITE = 1;
    public static final int POLISHED_GRANITE = 2;
    public static final int DIORITE = 3;
    public static final int POLISHED_DIORITE = 4;
    public static final int ANDESITE = 5;
    public static final int POLISHED_ANDESITE = 6;
    private static final String[] d = new String[]{"Stone", "Granite", "Polished Granite", "Diorite", "Polished Diorite", "Andesite", "Polished Andesite", "Unknown Stone"};

    public BlockStone() {
        this(0);
    }

    public BlockStone(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public double getResistance() {
        return 30.0;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public String getName() {
        return d[this.getDamage() & 7];
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            if (item.hasEnchantment(16)) {
                return new Item[]{this.toItem()};
            }
            return new Item[]{Item.get(this.getDamage() == 0 ? 4 : 1, this.getDamage(), 1)};
        }
        return new Item[0];
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public BlockColor getColor() {
        int n = this.getDamage() & 7;
        if (n == 1 || n == 2) {
            return BlockColor.DIRT_BLOCK_COLOR;
        }
        if (n == 3 || n == 4) {
            return BlockColor.QUARTZ_BLOCK_COLOR;
        }
        return BlockColor.STONE_BLOCK_COLOR;
    }
}

