/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockPressurePlateBase;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.utils.BlockColor;

public class BlockPressurePlateWood
extends BlockPressurePlateBase {
    public BlockPressurePlateWood(int n) {
        super(n);
        this.onPitch = 0.8f;
        this.offPitch = 0.7f;
    }

    public BlockPressurePlateWood() {
        this(0);
    }

    @Override
    public String getName() {
        return "Wooden Pressure Plate";
    }

    @Override
    public int getId() {
        return 72;
    }

    @Override
    public int getToolType() {
        return 4;
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
    public Item[] getDrops(Item item) {
        return new Item[]{this.toItem()};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    protected int computeRedstoneStrength() {
        AxisAlignedBB axisAlignedBB = this.getCollisionBoundingBox();
        for (Entity entity : this.level.getCollidingEntities(axisAlignedBB)) {
            if (!entity.doesTriggerPressurePlate()) continue;
            return 15;
        }
        return 0;
    }
}

