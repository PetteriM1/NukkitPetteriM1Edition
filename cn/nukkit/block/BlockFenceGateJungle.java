/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockFenceGate;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockFenceGateJungle
extends BlockFenceGate {
    public BlockFenceGateJungle() {
        this(0);
    }

    public BlockFenceGateJungle(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 185;
    }

    @Override
    public String getName() {
        return "Jungle Fence Gate";
    }

    @Override
    public Item toItem() {
        return Item.get(185, 0, 1);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }
}

