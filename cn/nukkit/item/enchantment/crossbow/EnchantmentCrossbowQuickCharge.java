/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.crossbow;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.crossbow.EnchantmentCrossbow;

public class EnchantmentCrossbowQuickCharge
extends EnchantmentCrossbow {
    public EnchantmentCrossbowQuickCharge() {
        super(35, "crossbowQuickCharge", Enchantment.Rarity.UNCOMMON);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 12 + 20 * (n - 1);
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return 50 + this.getMinEnchantAbility(n);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}

