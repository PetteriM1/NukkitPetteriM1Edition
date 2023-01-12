/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockMelon
extends BlockSolid {
    @Override
    public int getId() {
        return 103;
    }

    @Override
    public String getName() {
        return "Melon Block";
    }

    @Override
    public double getHardness() {
        return 1.0;
    }

    @Override
    public double getResistance() {
        return 5.0;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.hasEnchantment(16)) {
            return new Item[]{this.toItem()};
        }
        int n = 3 + Utils.random.nextInt(5);
        Enchantment enchantment = item.getEnchantment(18);
        if (enchantment != null && enchantment.getLevel() >= 1) {
            n += Utils.random.nextInt(enchantment.getLevel() + 1);
        }
        return new Item[]{Item.get(360, 0, Math.min(9, n))};
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.LIME_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

