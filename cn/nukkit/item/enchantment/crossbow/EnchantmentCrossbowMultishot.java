/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.crossbow;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.crossbow.EnchantmentCrossbow;

public class EnchantmentCrossbowMultishot
extends EnchantmentCrossbow {
    public EnchantmentCrossbowMultishot() {
        super(33, "crossbowMultishot", Enchantment.Rarity.RARE);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 20;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return 50 + this.getMinEnchantAbility(n);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && enchantment.id != 34;
    }
}

