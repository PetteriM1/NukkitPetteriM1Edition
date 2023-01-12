/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockWater;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

public class BlockWaterLily
extends BlockFlowable {
    public BlockWaterLily() {
        this(0);
    }

    public BlockWaterLily(int n) {
        super(0);
    }

    @Override
    public String getName() {
        return "Lily Pad";
    }

    @Override
    public int getId() {
        return 111;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.x + 0.0625, this.y, this.z + 0.0625, this.x + 0.9375, this.y + 0.015625, this.z + 0.9375);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Block block3;
        if (block2 instanceof BlockWater && (block3 = block2.up()).getId() == 0) {
            this.getLevel().setBlock(block3, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && !(this.down() instanceof BlockWater)) {
            this.getLevel().useBreakOn(this);
            return 1;
        }
        return 0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }

    @Override
    public boolean canPassThrough() {
        return false;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

