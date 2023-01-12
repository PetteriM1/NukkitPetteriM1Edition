/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.crossbow;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.crossbow.EnchantmentCrossbow;

public class EnchantmentCrossbowPiercing
extends EnchantmentCrossbow {
    public EnchantmentCrossbowPiercing() {
        super(34, "crossbowPiercing", Enchantment.Rarity.COMMON);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 1 + 10 * (n - 1);
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return 50 + this.getMinEnchantAbility(n);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && enchantment.id != 33;
    }
}

