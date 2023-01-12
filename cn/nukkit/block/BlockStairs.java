/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Faceable;

public abstract class BlockStairs
extends BlockSolidMeta
implements Faceable {
    private static final short[] d = new short[]{2, 1, 3, 0};

    protected BlockStairs(int n) {
        super(n);
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        if ((this.getDamage() & 4) > 0) {
            return new AxisAlignedBB(this.x, this.y + 0.5, this.z, this.x + 1.0, this.y + 1.0, this.z + 1.0);
        }
        return new AxisAlignedBB(this.x, this.y, this.z, this.x + 1.0, this.y + 0.5, this.z + 1.0);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.setDamage(d[player != null ? player.getDirection().getHorizontalIndex() : 0]);
        if (d3 > 0.5 && blockFace != BlockFace.UP || blockFace == BlockFace.DOWN) {
            this.setDamage(this.getDamage() | 4);
        }
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public Item toItem() {
        Item item = super.toItem();
        item.setDamage(0);
        return item;
    }

    @Override
    public boolean collidesWithBB(AxisAlignedBB axisAlignedBB) {
        int n = this.getDamage();
        int n2 = n & 3;
        double d2 = 0.0;
        double d3 = 0.5;
        double d4 = 0.5;
        double d5 = 1.0;
        if ((n & 4) > 0) {
            d2 = 0.5;
            d3 = 1.0;
            d4 = 0.0;
            d5 = 0.5;
        }
        if (axisAlignedBB.intersectsWith(new AxisAlignedBB(this.x, this.y + d2, this.z, this.x + 1.0, this.y + d3, this.z + 1.0))) {
            return true;
        }
        if (n2 == 0) {
            return axisAlignedBB.intersectsWith(new AxisAlignedBB(this.x + 0.5, this.y + d4, this.z, this.x + 1.0, this.y + d5, this.z + 1.0));
        }
        if (n2 == 1) {
            return axisAlignedBB.intersectsWith(new AxisAlignedBB(this.x, this.y + d4, this.z, this.x + 0.5, this.y + d5, this.z + 1.0));
        }
        if (n2 == 2) {
            return axisAlignedBB.intersectsWith(new AxisAlignedBB(this.x, this.y + d4, this.z + 0.5, this.x + 1.0, this.y + d5, this.z + 1.0));
        }
        if (n2 == 3) {
            return axisAlignedBB.intersectsWith(new AxisAlignedBB(this.x, this.y + d4, this.z, this.x + 1.0, this.y + d5, this.z + 0.5));
        }
        return false;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }
}

