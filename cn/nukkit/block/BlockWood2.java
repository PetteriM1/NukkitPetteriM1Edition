/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockWood;
import cn.nukkit.utils.BlockColor;

public class BlockWood2
extends BlockWood {
    public static final int ACACIA = 0;
    public static final int DARK_OAK = 1;
    private static final String[] f = new String[]{"Acacia Wood", "Dark Oak Wood", ""};

    public BlockWood2() {
        this(0);
    }

    public BlockWood2(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 162;
    }

    @Override
    public String getName() {
        return f[this.getDamage() > 2 ? 0 : this.getDamage()];
    }

    @Override
    public BlockColor getColor() {
        switch (this.getDamage() & 7) {
            case 0: {
                return BlockColor.ORANGE_BLOCK_COLOR;
            }
            case 1: {
                return BlockColor.BROWN_BLOCK_COLOR;
            }
        }
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    protected int getStrippedId() {
        int n = this.getDamage();
        if (n % 2 == 0) {
            return 263;
        }
        return 264;
    }
}

