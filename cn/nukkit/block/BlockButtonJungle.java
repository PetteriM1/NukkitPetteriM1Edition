/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockButtonWooden;

public class BlockButtonJungle
extends BlockButtonWooden {
    public BlockButtonJungle() {
        this(0);
    }

    public BlockButtonJungle(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Jungle Button";
    }

    @Override
    public int getId() {
        return 398;
    }
}

