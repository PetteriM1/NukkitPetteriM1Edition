/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockFallable;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockGravel
extends BlockFallable {
    @Override
    public int getId() {
        return 13;
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public double getResistance() {
        return 3.0;
    }

    @Override
    public int getToolType() {
        return 2;
    }

    @Override
    public String getName() {
        return "Gravel";
    }

    @Override
    public Item[] getDrops(Item item) {
        if (Utils.random.nextInt(9) == 0 && !item.hasEnchantment(16)) {
            return new Item[]{Item.get(318)};
        }
        return new Item[]{this.toItem()};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GRAY_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}

