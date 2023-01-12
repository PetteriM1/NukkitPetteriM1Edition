/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTransparent;
import cn.nukkit.item.Item;

public class BlockHardGlass
extends BlockTransparent {
    @Override
    public int getId() {
        return 253;
    }

    @Override
    public String getName() {
        return "Hardened Glass";
    }

    @Override
    public double getResistance() {
        return 1.5;
    }

    @Override
    public double getHardness() {
        return 0.3;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[0];
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}

