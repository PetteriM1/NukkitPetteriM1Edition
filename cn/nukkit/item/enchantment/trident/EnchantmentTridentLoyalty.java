/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.trident;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.trident.EnchantmentTrident;

public class EnchantmentTridentLoyalty
extends EnchantmentTrident {
    public EnchantmentTridentLoyalty() {
        super(31, "tridentLoyalty", Enchantment.Rarity.UNCOMMON);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 7 * n + 5;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}

