/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;

public class BlockCarpet
extends BlockFlowable {
    public BlockCarpet() {
        this(0);
    }

    public BlockCarpet(int n) {
        super(n);
    }

    public BlockCarpet(DyeColor dyeColor) {
        this(dyeColor.getWoolData());
    }

    @Override
    public int getId() {
        return 171;
    }

    @Override
    public double getHardness() {
        return 0.1;
    }

    @Override
    public double getResistance() {
        return 0.5;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public String getName() {
        return (Object)((Object)DyeColor.getByWoolData(this.getDamage())) + " Carpet";
    }

    @Override
    public boolean canPassThrough() {
        return false;
    }

    @Override
    public double getMaxY() {
        return this.y + 0.0625;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.getMinX(), this.getMinY(), this.getMinZ(), this.getMaxX(), this.getMaxY(), this.getMaxZ());
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Block block3 = this.down();
        if (block3.getId() != 0) {
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && this.down().getId() == 0) {
            this.getLevel().useBreakOn(this);
            return 1;
        }
        return 0;
    }

    @Override
    public BlockColor getColor() {
        return DyeColor.getByWoolData(this.getDamage()).getColor();
    }

    public DyeColor getDyeColor() {
        return DyeColor.getByWoolData(this.getDamage());
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

