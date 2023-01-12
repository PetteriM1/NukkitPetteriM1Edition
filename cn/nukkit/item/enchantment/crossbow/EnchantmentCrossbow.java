/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.crossbow;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public abstract class EnchantmentCrossbow
extends Enchantment {
    protected EnchantmentCrossbow(int n, String string, Enchantment.Rarity rarity) {
        super(n, string, rarity, EnchantmentType.CROSSBOW);
    }
}

