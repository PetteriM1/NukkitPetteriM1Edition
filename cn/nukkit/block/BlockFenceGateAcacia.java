/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockFenceGate;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockFenceGateAcacia
extends BlockFenceGate {
    public BlockFenceGateAcacia() {
        this(0);
    }

    public BlockFenceGateAcacia(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 187;
    }

    @Override
    public String getName() {
        return "Acacia Fence Gate";
    }

    @Override
    public Item toItem() {
        return Item.get(187, 0, 1);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
    }
}

