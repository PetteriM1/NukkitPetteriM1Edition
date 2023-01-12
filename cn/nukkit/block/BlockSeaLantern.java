/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTransparent;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockSeaLantern
extends BlockTransparent {
    @Override
    public String getName() {
        return "Sea Lantern";
    }

    @Override
    public int getId() {
        return 169;
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
    public int getLightLevel() {
        return 15;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.hasEnchantment(16)) {
            return new Item[]{this.toItem()};
        }
        return new Item[]{Item.get(422, 0, Utils.random.nextInt(2, 4))};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.QUARTZ_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}

