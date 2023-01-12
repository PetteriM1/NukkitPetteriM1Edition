/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockStairsSandstone;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;

public class BlockStairsSmoothSandstone
extends BlockStairsSandstone {
    public BlockStairsSmoothSandstone() {
        this(0);
    }

    public BlockStairsSmoothSandstone(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Smooth Sandstone Stairs";
    }

    @Override
    public int getId() {
        return 432;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }
}

