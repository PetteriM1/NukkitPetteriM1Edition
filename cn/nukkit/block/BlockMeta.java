/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;

public abstract class BlockMeta
extends Block {
    private int c;

    protected BlockMeta(int n) {
        this.c = n;
    }

    @Override
    public int getFullId() {
        return (this.getId() << 4) + this.c;
    }

    @Override
    public final int getDamage() {
        return this.c;
    }

    @Override
    public void setDamage(int n) {
        this.c = n;
    }
}

