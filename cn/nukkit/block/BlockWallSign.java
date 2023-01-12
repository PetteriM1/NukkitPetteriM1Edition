/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSignPost;
import cn.nukkit.math.BlockFace;

public class BlockWallSign
extends BlockSignPost {
    private static final int[] d = new int[]{3, 2, 5, 4};

    public BlockWallSign() {
        this(0);
    }

    public BlockWallSign(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 68;
    }

    @Override
    public String getName() {
        return "Wall Sign";
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && this.getDamage() >= 2 && this.getDamage() <= 5) {
            if (this.getSide(BlockFace.fromIndex(d[this.getDamage() - 2])).getId() == 0) {
                this.getLevel().useBreakOn(this);
            }
            return 1;
        }
        return 0;
    }
}

