/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public class EnchantmentMending
extends Enchantment {
    protected EnchantmentMending() {
        super(26, "mending", Enchantment.Rarity.RARE, EnchantmentType.BREAKABLE);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 25 * n;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return this.getMinEnchantAbility(n) + 50;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && enchantment.id != 22;
    }
}

