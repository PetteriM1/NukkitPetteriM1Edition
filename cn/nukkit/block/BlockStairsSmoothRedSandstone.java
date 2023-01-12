/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockStairsRedSandstone;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;

public class BlockStairsSmoothRedSandstone
extends BlockStairsRedSandstone {
    public BlockStairsSmoothRedSandstone() {
        this(0);
    }

    public BlockStairsSmoothRedSandstone(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Smooth RedSand stone Stairs";
    }

    @Override
    public int getId() {
        return 431;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }
}

