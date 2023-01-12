/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public class EnchantmentSilkTouch
extends Enchantment {
    protected EnchantmentSilkTouch() {
        super(16, "untouching", Enchantment.Rarity.VERY_RARE, EnchantmentType.DIGGER);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 15;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return super.getMinEnchantAbility(n) + 50;
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && enchantment.id != 18;
    }

    @Override
    public boolean canEnchant(Item item) {
        return item.isShears() || super.canEnchant(item);
    }
}

