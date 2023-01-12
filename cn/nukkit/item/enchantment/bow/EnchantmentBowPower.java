/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.bow;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.bow.EnchantmentBow;

public class EnchantmentBowPower
extends EnchantmentBow {
    public EnchantmentBowPower() {
        super(19, "arrowDamage", Enchantment.Rarity.COMMON);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 1 + (n - 1) * 10;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return this.getMinEnchantAbility(n) + 15;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}

