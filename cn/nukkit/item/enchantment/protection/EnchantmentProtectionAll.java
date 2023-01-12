/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.protection;

import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.protection.EnchantmentProtection;

public class EnchantmentProtectionAll
extends EnchantmentProtection {
    public EnchantmentProtectionAll() {
        super(0, "all", Enchantment.Rarity.COMMON, EnchantmentProtection.TYPE.ALL);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 1 + (n - 1) * 11;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return this.getMinEnchantAbility(n) + 20;
    }

    @Override
    public double getTypeModifier() {
        return 1.0;
    }

    @Override
    public float getProtectionFactor(EntityDamageEvent entityDamageEvent) {
        EntityDamageEvent.DamageCause damageCause = entityDamageEvent.getCause();
        if (this.level <= 0 || damageCause == EntityDamageEvent.DamageCause.VOID || damageCause == EntityDamageEvent.DamageCause.CUSTOM || damageCause == EntityDamageEvent.DamageCause.HUNGER) {
            return 0.0f;
        }
        return (float)((double)this.getLevel() * this.getTypeModifier());
    }
}

