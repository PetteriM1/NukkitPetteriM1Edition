/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockThin;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockHardGlassPane
extends BlockThin {
    @Override
    public String getName() {
        return "Hardened Glass Pane";
    }

    @Override
    public int getId() {
        return 190;
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
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}

