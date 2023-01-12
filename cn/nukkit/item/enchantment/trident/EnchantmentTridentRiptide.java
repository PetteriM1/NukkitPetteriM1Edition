/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.trident;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.trident.EnchantmentTrident;

public class EnchantmentTridentRiptide
extends EnchantmentTrident {
    public EnchantmentTridentRiptide() {
        super(30, "tridentRiptide", Enchantment.Rarity.RARE);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 7 * n + 10;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}

