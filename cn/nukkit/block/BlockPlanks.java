/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.utils.BlockColor;

public class BlockPlanks
extends BlockSolidMeta {
    public static final int OAK = 0;
    public static final int SPRUCE = 1;
    public static final int BIRCH = 2;
    public static final int JUNGLE = 3;
    public static final int ACACIA = 4;
    public static final int DARK_OAK = 5;

    public BlockPlanks() {
        this(0);
    }

    public BlockPlanks(int n) {
        super(n % 6);
    }

    @Override
    public int getId() {
        return 5;
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
    public int getBurnChance() {
        return 5;
    }

    @Override
    public int getBurnAbility() {
        return 20;
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Oak Wood Planks", "Spruce Wood Planks", "Birch Wood Planks", "Jungle Wood Planks", "Acacia Wood Planks", "Dark Oak Wood Planks"};
        return stringArray[this.getDamage() & 7];
    }

    @Override
    public int getToolType() {
        return 4;
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

