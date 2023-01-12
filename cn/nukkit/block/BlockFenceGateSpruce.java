/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockFenceGate;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockFenceGateSpruce
extends BlockFenceGate {
    public BlockFenceGateSpruce() {
        this(0);
    }

    public BlockFenceGateSpruce(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 183;
    }

    @Override
    public String getName() {
        return "Spruce Fence Gate";
    }

    @Override
    public Item toItem() {
        return Item.get(183, 0, 1);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SPRUCE_BLOCK_COLOR;
    }
}

