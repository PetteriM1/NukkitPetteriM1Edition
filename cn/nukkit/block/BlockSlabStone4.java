/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSlabStone;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;

public class BlockSlabStone4
extends BlockSlabStone {
    public BlockSlabStone4() {
        this(0);
    }

    public BlockSlabStone4(int n) {
        super(n, 423);
    }

    @Override
    public String getName() {
        return "Mossy Stone Brick Slab";
    }

    @Override
    public int getId() {
        return 421;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, this.getDamage() & 7);
    }
}

