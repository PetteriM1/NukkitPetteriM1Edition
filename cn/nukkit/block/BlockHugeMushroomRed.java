/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockHugeMushroomRed
extends BlockSolidMeta {
    public BlockHugeMushroomRed() {
        this(0);
    }

    public BlockHugeMushroomRed(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Red Mushroom Block";
    }

    @Override
    public int getId() {
        return 100;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public double getHardness() {
        return 0.2;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item != null && item.hasEnchantment(16)) {
            return new Item[]{this.toItem()};
        }
        return new Item[]{new ItemBlock(Block.get(40), 0, Utils.rand() ? Utils.rand(0, 2) : 0)};
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.RED_BLOCK_COLOR;
    }
}

