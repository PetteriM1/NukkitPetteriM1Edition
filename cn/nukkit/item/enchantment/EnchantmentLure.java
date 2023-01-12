/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public class EnchantmentLure
extends Enchantment {
    protected EnchantmentLure() {
        super(24, "fishingSpeed", Enchantment.Rarity.RARE, EnchantmentType.FISHING_ROD);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return n + 8 * n + 6;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}

