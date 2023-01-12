/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.bow;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.bow.EnchantmentBow;

public class EnchantmentBowFlame
extends EnchantmentBow {
    public EnchantmentBowFlame() {
        super(21, "arrowFire", Enchantment.Rarity.RARE);
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

