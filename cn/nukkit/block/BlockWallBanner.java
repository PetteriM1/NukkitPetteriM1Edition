/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockBanner;
import cn.nukkit.math.BlockFace;

public class BlockWallBanner
extends BlockBanner {
    public BlockWallBanner() {
        this(0);
    }

    public BlockWallBanner(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 177;
    }

    @Override
    public String getName() {
        return "Wall Banner";
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && this.getDamage() >= BlockFace.NORTH.getIndex() && this.getDamage() <= BlockFace.EAST.getIndex()) {
            if (this.getSide(BlockFace.fromIndex(this.getDamage()).getOpposite()).getId() == 0) {
                this.getLevel().useBreakOn(this);
            }
            return 1;
        }
        return 0;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromIndex(this.getDamage() & 7);
    }
}

