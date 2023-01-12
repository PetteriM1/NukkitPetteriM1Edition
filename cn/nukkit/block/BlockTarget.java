/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockTarget
extends BlockSolidMeta {
    public BlockTarget() {
        this(0);
    }

    public BlockTarget(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Target";
    }

    @Override
    public int getId() {
        return 494;
    }

    @Override
    public int getToolType() {
        return 6;
    }

    @Override
    public int getBurnChance() {
        return 5;
    }

    @Override
    public int getBurnAbility() {
        return 15;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 0.5;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WHITE_BLOCK_COLOR;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{new ItemBlock(Block.get(494))};
    }
}

