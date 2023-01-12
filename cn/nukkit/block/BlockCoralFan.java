/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockCoral;

public class BlockCoralFan
extends BlockCoral {
    public BlockCoralFan() {
        this(0);
    }

    public BlockCoralFan(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Coral Fan";
    }

    @Override
    public int getId() {
        return 388;
    }
}

