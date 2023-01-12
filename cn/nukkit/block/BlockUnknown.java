/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockMeta;

public class BlockUnknown
extends BlockMeta {
    private final int d;

    public BlockUnknown(int n) {
        this(n, (Integer)0);
    }

    public BlockUnknown(int n, Integer n2) {
        super(n2);
        this.d = n;
    }

    @Override
    public int getId() {
        return this.d;
    }

    @Override
    public String getName() {
        return "Unknown";
    }
}

