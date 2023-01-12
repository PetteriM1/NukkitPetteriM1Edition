/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.bow;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public abstract class EnchantmentBow
extends Enchantment {
    protected EnchantmentBow(int n, String string, Enchantment.Rarity rarity) {
        super(n, string, rarity, EnchantmentType.BOW);
    }
}

