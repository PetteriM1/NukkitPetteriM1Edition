/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockButtonWooden;

public class BlockButtonSpruce
extends BlockButtonWooden {
    public BlockButtonSpruce() {
        this(0);
    }

    public BlockButtonSpruce(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Spruce Button";
    }

    @Override
    public int getId() {
        return 399;
    }
}

