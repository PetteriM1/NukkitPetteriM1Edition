/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.trident;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public abstract class EnchantmentTrident
extends Enchantment {
    protected EnchantmentTrident(int n, String string, Enchantment.Rarity rarity) {
        super(n, string, rarity, EnchantmentType.TRIDENT);
    }
}

