/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.trident;

import cn.nukkit.entity.Entity;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.trident.EnchantmentTrident;

public class EnchantmentTridentImpaling
extends EnchantmentTrident {
    public EnchantmentTridentImpaling() {
        super(29, "tridentImpaling", Enchantment.Rarity.RARE);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 8 * n - 7;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return this.getMinEnchantAbility(n) + 20;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public double getDamageBonus(Entity entity) {
        if (entity.isInsideOfWater() || entity.getLevel().isRaining() && entity.canSeeSky()) {
            return 2.5 * (double)this.getLevel();
        }
        return 0.0;
    }
}

