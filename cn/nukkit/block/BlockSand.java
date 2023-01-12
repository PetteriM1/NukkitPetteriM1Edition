/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockFallableMeta;
import cn.nukkit.utils.BlockColor;

public class BlockSand
extends BlockFallableMeta {
    public static final int DEFAULT = 0;
    public static final int RED = 1;

    public BlockSand() {
        this(0);
    }

    public BlockSand(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 12;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public int getToolType() {
        return 2;
    }

    @Override
    public String getName() {
        if (this.getDamage() == 1) {
            return "Red Sand";
        }
        return "Sand";
    }

    @Override
    public BlockColor getColor() {
        if (this.getDamage() == 1) {
            return BlockColor.ORANGE_BLOCK_COLOR;
        }
        return BlockColor.SAND_BLOCK_COLOR;
    }
}

