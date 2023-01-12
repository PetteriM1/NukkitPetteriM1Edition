/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockFlowable;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.utils.BlockColor;

public class BlockCobweb
extends BlockFlowable {
    public BlockCobweb() {
        this(0);
    }

    public BlockCobweb(int n) {
        super(0);
    }

    @Override
    public String getName() {
        return "Cobweb";
    }

    @Override
    public int getId() {
        return 30;
    }

    @Override
    public double getHardness() {
        return 4.0;
    }

    @Override
    public double getResistance() {
        return 20.0;
    }

    @Override
    public int getToolType() {
        return 1;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        entity.resetFallDistance();
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isShears()) {
            return new Item[]{this.toItem()};
        }
        if (item.isSword()) {
            return new Item[]{Item.get(287)};
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CLOTH_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.getMinX(), this.getMinY(), this.getMinZ(), this.getMaxX(), this.getMaxY(), this.getMaxZ());
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

