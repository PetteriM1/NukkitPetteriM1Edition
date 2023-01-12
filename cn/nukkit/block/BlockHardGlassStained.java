/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.item.Item;

public class BlockHardGlassStained
extends BlockTransparentMeta {
    public BlockHardGlassStained() {
        this(0);
    }

    public BlockHardGlassStained(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 254;
    }

    @Override
    public String getName() {
        return "Hardened Stained Glass";
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

