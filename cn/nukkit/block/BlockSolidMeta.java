/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockMeta;
import cn.nukkit.utils.BlockColor;

public abstract class BlockSolidMeta
extends BlockMeta {
    protected BlockSolidMeta(int n) {
        super(n);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.STONE_BLOCK_COLOR;
    }
}

