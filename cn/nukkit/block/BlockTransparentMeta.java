/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockMeta;
import cn.nukkit.utils.BlockColor;

public abstract class BlockTransparentMeta
extends BlockMeta {
    protected BlockTransparentMeta() {
        this(0);
    }

    protected BlockTransparentMeta(int n) {
        super(n);
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.TRANSPARENT_BLOCK_COLOR;
    }
}

