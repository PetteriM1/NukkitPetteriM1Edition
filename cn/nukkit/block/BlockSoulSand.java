/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.entity.Entity;
import cn.nukkit.utils.BlockColor;

public class BlockSoulSand
extends BlockSolid {
    @Override
    public String getName() {
        return "Soul Sand";
    }

    @Override
    public int getId() {
        return 88;
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
        return 2;
    }

    @Override
    public double getMaxY() {
        return this.y + 0.875;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        entity.motionX *= 0.4;
        entity.motionZ *= 0.4;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.BROWN_BLOCK_COLOR;
    }
}

