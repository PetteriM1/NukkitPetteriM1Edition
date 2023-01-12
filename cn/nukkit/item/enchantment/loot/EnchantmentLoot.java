/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.loot;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public abstract class EnchantmentLoot
extends Enchantment {
    protected EnchantmentLoot(int n, String string, Enchantment.Rarity rarity, EnchantmentType enchantmentType) {
        super(n, string, rarity, enchantmentType);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 15 + (n - 1) * 9;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return this.getMinEnchantAbility(n) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && enchantment.id != 16;
    }
}

