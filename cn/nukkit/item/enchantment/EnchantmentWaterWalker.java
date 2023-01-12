/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public class EnchantmentWaterWalker
extends Enchantment {
    protected EnchantmentWaterWalker() {
        super(7, "waterWalker", Enchantment.Rarity.RARE, EnchantmentType.ARMOR_FEET);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return n * 10;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return this.getMinEnchantAbility(n) + 10;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}

