/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockFenceGate;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockFenceGateDarkOak
extends BlockFenceGate {
    public BlockFenceGateDarkOak() {
        this(0);
    }

    public BlockFenceGateDarkOak(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 186;
    }

    @Override
    public String getName() {
        return "Dark Oak Fence Gate";
    }

    @Override
    public Item toItem() {
        return Item.get(186, 0, 1);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.BROWN_BLOCK_COLOR;
    }
}

