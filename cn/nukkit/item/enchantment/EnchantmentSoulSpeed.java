/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public class EnchantmentSoulSpeed
extends Enchantment {
    protected EnchantmentSoulSpeed() {
        super(36, "soul_speed", Enchantment.Rarity.VERY_RARE, EnchantmentType.ARMOR_FEET);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 10 * n;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}

