/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.protection;

import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.protection.EnchantmentProtection;

public class EnchantmentProtectionFall
extends EnchantmentProtection {
    public EnchantmentProtectionFall() {
        super(2, "fall", Enchantment.Rarity.UNCOMMON, EnchantmentProtection.TYPE.FALL);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 5 + (n - 1) * 6;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return this.getMinEnchantAbility(n) + 10;
    }

    @Override
    public double getTypeModifier() {
        return 2.0;
    }

    @Override
    public float getProtectionFactor(EntityDamageEvent entityDamageEvent) {
        EntityDamageEvent.DamageCause damageCause = entityDamageEvent.getCause();
        if (this.level <= 0 || damageCause != EntityDamageEvent.DamageCause.FALL) {
            return 0.0f;
        }
        return (float)((double)this.getLevel() * this.getTypeModifier());
    }
}

