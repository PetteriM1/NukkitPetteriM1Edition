/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFence;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockLeaves;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.block.BlockSlab;
import cn.nukkit.block.BlockStairs;
import cn.nukkit.block.BlockWall;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

public class BlockLantern
extends BlockFlowable {
    public BlockLantern() {
        this(0);
    }

    public BlockLantern(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Lantern";
    }

    @Override
    public int getId() {
        return 463;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public double getResistance() {
        return 3.5;
    }

    @Override
    public double getHardness() {
        return 3.5;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public boolean canPassThrough() {
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.x + 0.2, this.y, this.z + 0.2, this.x + 0.8, this.y + 0.45, this.z + 0.8);
    }

    private boolean b() {
        Block block = this.up();
        switch (block.getId()) {
            case 101: 
            case 154: {
                return true;
            }
        }
        if (block instanceof BlockFence) {
            return true;
        }
        if (block instanceof BlockSlab && (block.getDamage() & 8) == 0) {
            return true;
        }
        if (block instanceof BlockStairs && (block.getDamage() & 4) == 0) {
            return true;
        }
        return !block.isTransparent() && block.isSolid() && !block.isPowerSource();
    }

    private boolean a() {
        Block block = this.down();
        if (block instanceof BlockLeaves) {
            return false;
        }
        if (block instanceof BlockFence || block instanceof BlockWall) {
            return true;
        }
        if (block instanceof BlockSlab) {
            return (block.getDamage() & 8) == 8;
        }
        if (block instanceof BlockStairs) {
            return (block.getDamage() & 4) == 4;
        }
        return block.isSolid();
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        boolean bl;
        if (this.getLevelBlock() instanceof BlockLiquid) {
            return false;
        }
        boolean bl2 = this.a();
        boolean bl3 = bl = blockFace != BlockFace.UP && this.b() && (!bl2 || blockFace == BlockFace.DOWN);
        if (!bl2 && !bl) {
            return false;
        }
        if (bl) {
            this.setDamage(1);
        } else {
            this.setDamage(0);
        }
        this.getLevel().setBlock(this, this, true, true);
        return true;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            if (this.getDamage() == 0) {
                if (!this.a()) {
                    this.level.useBreakOn(this);
                }
            } else if (!this.b()) {
                this.level.useBreakOn(this);
            }
            return n;
        }
        return 0;
    }
}

