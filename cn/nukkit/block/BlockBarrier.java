/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTransparent;
import cn.nukkit.item.Item;

public class BlockBarrier
extends BlockTransparent {
    @Override
    public String getName() {
        return "Barrier";
    }

    @Override
    public int getId() {
        return 416;
    }

    @Override
    public double getResistance() {
        return 3600000.0;
    }

    @Override
    public double getHardness() {
        return -1.0;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

