/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public class EnchantmentKnockback
extends Enchantment {
    protected EnchantmentKnockback() {
        super(12, "knockback", Enchantment.Rarity.UNCOMMON, EnchantmentType.SWORD);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 5 + (n - 1) * 20;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return super.getMinEnchantAbility(n) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}

