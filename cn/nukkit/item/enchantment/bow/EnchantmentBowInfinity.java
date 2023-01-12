/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.bow;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.bow.EnchantmentBow;

public class EnchantmentBowInfinity
extends EnchantmentBow {
    public EnchantmentBowInfinity() {
        super(22, "arrowInfinite", Enchantment.Rarity.VERY_RARE);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 20;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return 50;
    }
}

