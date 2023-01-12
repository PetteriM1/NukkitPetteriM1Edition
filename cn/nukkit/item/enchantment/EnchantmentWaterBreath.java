/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public class EnchantmentWaterBreath
extends Enchantment {
    protected EnchantmentWaterBreath() {
        super(6, "oxygen", Enchantment.Rarity.RARE, EnchantmentType.ARMOR_HEAD);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 10 * n;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return this.getMinEnchantAbility(n) + 30;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}

