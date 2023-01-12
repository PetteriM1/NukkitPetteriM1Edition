/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockFenceGate;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockFenceGateBirch
extends BlockFenceGate {
    public BlockFenceGateBirch() {
        this(0);
    }

    public BlockFenceGateBirch(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 184;
    }

    @Override
    public String getName() {
        return "Birch Fence Gate";
    }

    @Override
    public Item toItem() {
        return Item.get(184, 0, 1);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }
}

