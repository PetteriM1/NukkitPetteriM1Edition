/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public class EnchantmentBindingCurse
extends Enchantment {
    protected EnchantmentBindingCurse() {
        super(27, "curse.binding", Enchantment.Rarity.VERY_RARE, EnchantmentType.WEARABLE);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 25;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return 30;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }
}

