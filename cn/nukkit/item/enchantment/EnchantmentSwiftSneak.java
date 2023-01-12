/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public class EnchantmentSwiftSneak
extends Enchantment {
    protected EnchantmentSwiftSneak() {
        super(37, "swift_sneak", Enchantment.Rarity.VERY_RARE, EnchantmentType.ARMOR_LEGS);
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

