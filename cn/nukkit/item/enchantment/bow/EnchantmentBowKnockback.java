/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.bow;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.bow.EnchantmentBow;

public class EnchantmentBowKnockback
extends EnchantmentBow {
    public EnchantmentBowKnockback() {
        super(20, "arrowKnockback", Enchantment.Rarity.RARE);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return this.getMinEnchantAbility(n) + 25;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return this.getMinEnchantAbility(n) + 25;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}

