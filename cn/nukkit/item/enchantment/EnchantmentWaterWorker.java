/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public class EnchantmentWaterWorker
extends Enchantment {
    protected EnchantmentWaterWorker() {
        super(8, "waterWorker", Enchantment.Rarity.RARE, EnchantmentType.ARMOR_HEAD);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 1;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return this.getMinEnchantAbility(n) + 40;
    }
}

