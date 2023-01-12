/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockButtonWooden;

public class BlockButtonDarkOak
extends BlockButtonWooden {
    public BlockButtonDarkOak() {
        this(0);
    }

    public BlockButtonDarkOak(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Dark Oak Button";
    }

    @Override
    public int getId() {
        return 397;
    }
}

