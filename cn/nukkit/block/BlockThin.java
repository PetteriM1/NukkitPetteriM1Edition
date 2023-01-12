/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparent;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.utils.LevelException;

public abstract class BlockThin
extends BlockTransparent {
    protected BlockThin() {
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        double d2 = 0.4375;
        double d3 = 0.5625;
        double d4 = 0.4375;
        double d5 = 0.5625;
        try {
            boolean bl = this.canConnect(this.north());
            boolean bl2 = this.canConnect(this.south());
            boolean bl3 = this.canConnect(this.west());
            boolean bl4 = this.canConnect(this.east());
            if ((!bl3 || !bl4) && (bl3 || bl4 || bl || bl2)) {
                if (bl3) {
                    d2 = 0.0;
                } else if (bl4) {
                    d3 = 1.0;
                }
            } else {
                d2 = 0.0;
                d3 = 1.0;
            }
            if ((!bl || !bl2) && (bl3 || bl4 || bl || bl2)) {
                if (bl) {
                    d4 = 0.0;
                } else if (bl2) {
                    d5 = 1.0;
                }
            } else {
                d4 = 0.0;
                d5 = 1.0;
            }
        }
        catch (LevelException levelException) {
            // empty catch block
        }
        return new AxisAlignedBB(this.x + d2, this.y, this.z + d4, this.x + d3, this.y + 1.0, this.z + d5);
    }

    public boolean canConnect(Block block) {
        return block.isSolid() || block.getId() == this.getId() || block.getId() == 102 || block.getId() == 20;
    }

    private static LevelException a(LevelException levelException) {
        return levelException;
    }
}

