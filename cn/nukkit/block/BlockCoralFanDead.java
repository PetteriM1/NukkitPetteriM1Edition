/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockCoralFan;

public class BlockCoralFanDead
extends BlockCoralFan {
    public BlockCoralFanDead() {
        this(0);
    }

    public BlockCoralFanDead(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Dead Coral Fan";
    }

    @Override
    public int getId() {
        return 389;
    }
}

