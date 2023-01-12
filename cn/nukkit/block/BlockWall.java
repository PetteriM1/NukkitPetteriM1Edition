/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;

public class BlockWall
extends BlockTransparentMeta {
    public static final int NONE_MOSSY_WALL = 0;
    public static final int MOSSY_WALL = 1;

    public BlockWall() {
        this(0);
    }

    public BlockWall(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 139;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public double getHardness() {
        return 2.0;
    }

    @Override
    public double getResistance() {
        return 30.0;
    }

    @Override
    public String getName() {
        if (this.getDamage() == 1) {
            return "Mossy Cobblestone Wall";
        }
        return "Cobblestone Wall";
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        double d2;
        boolean bl = this.canConnect(this.getSide(BlockFace.NORTH));
        boolean bl2 = this.canConnect(this.getSide(BlockFace.SOUTH));
        boolean bl3 = this.canConnect(this.getSide(BlockFace.WEST));
        boolean bl4 = this.canConnect(this.getSide(BlockFace.EAST));
        double d3 = bl ? 0.0 : 0.25;
        double d4 = bl2 ? 1.0 : 0.75;
        double d5 = bl3 ? 0.0 : 0.25;
        double d6 = d2 = bl4 ? 1.0 : 0.75;
        if (bl && bl2 && !bl3 && !bl4) {
            d5 = 0.3125;
            d2 = 0.6875;
        } else if (!bl && !bl2 && bl3 && bl4) {
            d3 = 0.3125;
            d4 = 0.6875;
        }
        return new AxisAlignedBB(this.x + d5, this.y, this.z + d3, this.x + d2, this.y + 1.5, this.z + d4);
    }

    public boolean canConnect(Block block) {
        return block.getId() == 139 || block.getId() == 107 || block.isSolid() && !block.isTransparent();
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

