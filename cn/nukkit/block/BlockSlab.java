/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;

public abstract class BlockSlab
extends BlockTransparentMeta {
    protected final int doubleSlab;

    public BlockSlab(int n, int n2) {
        super(n);
        this.doubleSlab = n2;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        if ((this.getDamage() & 8) > 0) {
            return new AxisAlignedBB(this.x, this.y + 0.5, this.z, this.x + 1.0, this.y + 1.0, this.z + 1.0);
        }
        return new AxisAlignedBB(this.x, this.y, this.z, this.x + 1.0, this.y + 0.5, this.z + 1.0);
    }

    @Override
    public double getHardness() {
        return 2.0;
    }

    @Override
    public double getResistance() {
        return this.getToolType() < 4 ? 30.0 : 15.0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.setDamage(this.getDamage() & 7);
        if (blockFace == BlockFace.DOWN) {
            if (block2 instanceof BlockSlab && ((BlockSlab)block2).doubleSlab == this.doubleSlab && (block2.getDamage() & 8) == 8 && (block2.getDamage() & 7) == (this.getDamage() & 7)) {
                this.getLevel().setBlock(block2, Block.get(this.doubleSlab, this.getDamage()), true);
                return true;
            }
            if (block instanceof BlockSlab && ((BlockSlab)block).doubleSlab == this.doubleSlab && (block.getDamage() & 7) == (this.getDamage() & 7)) {
                this.getLevel().setBlock(block, Block.get(this.doubleSlab, this.getDamage()), true);
                return true;
            }
            this.setDamage(this.getDamage() | 8);
        } else if (blockFace == BlockFace.UP) {
            if (block2 instanceof BlockSlab && ((BlockSlab)block2).doubleSlab == this.doubleSlab && (block2.getDamage() & 8) == 0 && (block2.getDamage() & 7) == (this.getDamage() & 7)) {
                this.getLevel().setBlock(block2, Block.get(this.doubleSlab, this.getDamage()), true);
                return true;
            }
            if (block instanceof BlockSlab && ((BlockSlab)block).doubleSlab == this.doubleSlab && (block.getDamage() & 7) == (this.getDamage() & 7)) {
                this.getLevel().setBlock(block, Block.get(this.doubleSlab, this.getDamage()), true);
                return true;
            }
        } else {
            if (block instanceof BlockSlab && ((BlockSlab)block).doubleSlab == this.doubleSlab) {
                if ((block.getDamage() & 7) == (this.getDamage() & 7)) {
                    this.getLevel().setBlock(block, Block.get(this.doubleSlab, this.getDamage()), true);
                    return true;
                }
                return false;
            }
            if (d3 > 0.5) {
                this.setDamage(this.getDamage() | 8);
            }
        }
        if (block instanceof BlockSlab && ((BlockSlab)block).doubleSlab == this.doubleSlab && (block2.getDamage() & 7) != (this.getDamage() & 7)) {
            return false;
        }
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public boolean isTransparent() {
        return (this.getDamage() & 8) <= 0;
    }
}

