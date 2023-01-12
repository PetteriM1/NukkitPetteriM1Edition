/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public class EnchantmentEfficiency
extends Enchantment {
    protected EnchantmentEfficiency() {
        super(15, "digging", Enchantment.Rarity.COMMON, EnchantmentType.DIGGER);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 1 + (n - 1) * 10;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return super.getMinEnchantAbility(n) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean canEnchant(Item item) {
        return item.isShears() || super.canEnchant(item);
    }
}

