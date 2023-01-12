/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTransparent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.math.MathHelper;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockGlowstone
extends BlockTransparent {
    @Override
    public String getName() {
        return "Glowstone";
    }

    @Override
    public int getId() {
        return 89;
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
        int n = 2 + Utils.random.nextInt(3);
        Enchantment enchantment = item.getEnchantment(18);
        if (enchantment != null && enchantment.getLevel() >= 1) {
            n += Utils.random.nextInt(enchantment.getLevel() + 1);
        }
        return new Item[]{Item.get(348, 0, MathHelper.clamp(n, 1, 4))};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}

