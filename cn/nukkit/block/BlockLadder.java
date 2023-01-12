/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public class BlockLadder
extends BlockTransparentMeta
implements Faceable {
    private static final int[] d = new int[]{0, 1, 3, 2, 5, 4};

    public BlockLadder() {
        this(0);
    }

    public BlockLadder(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Ladder";
    }

    @Override
    public int getId() {
        return 65;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public boolean canBeClimbed() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public double getHardness() {
        return 0.4;
    }

    @Override
    public double getResistance() {
        return 2.0;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        double d2 = 0.1875;
        if (this.getDamage() == 2) {
            return new AxisAlignedBB(this.x, this.y, this.z + 1.0 - d2, this.x + 1.0, this.y + 1.0, this.z + 1.0);
        }
        if (this.getDamage() == 3) {
            return new AxisAlignedBB(this.x, this.y, this.z, this.x + 1.0, this.y + 1.0, this.z + d2);
        }
        if (this.getDamage() == 4) {
            return new AxisAlignedBB(this.x + 1.0 - d2, this.y, this.z, this.x + 1.0, this.y + 1.0, this.z + 1.0);
        }
        if (this.getDamage() == 5) {
            return new AxisAlignedBB(this.x, this.y, this.z, this.x + d2, this.y + 1.0, this.z + 1.0);
        }
        return null;
    }

    @Override
    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return super.recalculateBoundingBox();
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (!block2.isTransparent() && blockFace.getIndex() >= 2 && blockFace.getIndex() <= 5) {
            this.setDamage(blockFace.getIndex());
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && !this.getSide(BlockFace.fromIndex(d[this.getDamage()])).isSolid()) {
            this.getLevel().useBreakOn(this);
            return 1;
        }
        return 0;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{Item.get(65)};
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }

    @Override
    public void onEntityCollide(Entity entity) {
        entity.resetFallDistance();
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

