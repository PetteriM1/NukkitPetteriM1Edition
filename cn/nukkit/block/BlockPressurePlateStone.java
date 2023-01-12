/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockPressurePlateBase;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.utils.BlockColor;

public class BlockPressurePlateStone
extends BlockPressurePlateBase {
    public BlockPressurePlateStone(int n) {
        super(n);
        this.onPitch = 0.6f;
        this.offPitch = 0.5f;
    }

    public BlockPressurePlateStone() {
        this(0);
    }

    @Override
    public String getName() {
        return "Stone Pressure Plate";
    }

    @Override
    public int getId() {
        return 70;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.STONE_BLOCK_COLOR;
    }

    @Override
    protected int computeRedstoneStrength() {
        AxisAlignedBB axisAlignedBB = this.getCollisionBoundingBox();
        for (Entity entity : this.level.getCollidingEntities(axisAlignedBB)) {
            if (!(entity instanceof EntityLiving) || !entity.doesTriggerPressurePlate()) continue;
            return 15;
        }
        return 0;
    }
}

