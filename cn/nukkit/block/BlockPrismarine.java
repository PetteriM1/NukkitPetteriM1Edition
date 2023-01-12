/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockPrismarine
extends BlockSolidMeta {
    public static final int NORMAL = 0;
    public static final int DARK = 1;
    public static final int BRICKS = 2;
    private static final String[] d = new String[]{"Prismarine", "Dark Prismarine", "Prismarine Bricks"};

    public BlockPrismarine() {
        this(0);
    }

    public BlockPrismarine(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 168;
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
        return d[this.getDamage() > 2 ? 0 : this.getDamage()];
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{this.toItem()};
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
                return BlockColor.CYAN_BLOCK_COLOR;
            }
            case 1: 
            case 2: {
                return BlockColor.DIAMOND_BLOCK_COLOR;
            }
        }
        return BlockColor.STONE_BLOCK_COLOR;
    }
}

