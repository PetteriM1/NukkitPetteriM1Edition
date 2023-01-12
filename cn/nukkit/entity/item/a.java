/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockWater;
import cn.nukkit.entity.item.EntityBoat;
import cn.nukkit.math.AxisAlignedBB;

class a
implements AxisAlignedBB.BBConsumer<Double> {
    private double a;
    final double val$maxY;
    final EntityBoat this$0;

    a(EntityBoat entityBoat, double d2) {
        this.this$0 = entityBoat;
        this.val$maxY = d2;
        this.a = Double.MAX_VALUE;
    }

    @Override
    public void accept(int n, int n2, int n3) {
        Block block = this.this$0.level.getBlock(this.this$0.temporalVector.setComponents(n, n2, n3));
        if (block instanceof BlockWater) {
            this.a = Math.min(this.val$maxY - block.getMaxY(), this.a);
        }
    }

    @Override
    public Double get() {
        return this.a;
    }
}

