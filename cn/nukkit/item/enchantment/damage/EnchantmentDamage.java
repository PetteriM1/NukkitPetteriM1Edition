/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.damage;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public abstract class EnchantmentDamage
extends Enchantment {
    protected EnchantmentDamage(int n, String string, Enchantment.Rarity rarity, TYPE tYPE) {
        super(n, string, rarity, EnchantmentType.SWORD);
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return !(enchantment instanceof EnchantmentDamage);
    }

    @Override
    public boolean canEnchant(Item item) {
        return item.isAxe() || super.canEnchant(item);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public String getName() {
        return "%enchantment.damage." + this.name;
    }

    @Override
    public boolean isMajor() {
        return true;
    }

    public static enum TYPE {
        ALL,
        SMITE,
        ARTHROPODS;

    }
}

