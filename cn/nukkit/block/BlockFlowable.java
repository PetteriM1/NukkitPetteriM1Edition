/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.math.AxisAlignedBB;

public abstract class BlockFlowable
extends BlockTransparentMeta {
    protected BlockFlowable(int n) {
        super(n);
    }

    @Override
    public boolean canBeFlowedInto() {
        return true;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0.0;
    }

    @Override
    public double getResistance() {
        return 0.0;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return null;
    }
}

