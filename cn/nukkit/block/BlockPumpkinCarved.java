/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockPumpkin;

public class BlockPumpkinCarved
extends BlockPumpkin {
    @Override
    public String getName() {
        return "Carved Pumpkin";
    }

    @Override
    public int getId() {
        return 410;
    }

    @Override
    public boolean canBeActivated() {
        return false;
    }
}

