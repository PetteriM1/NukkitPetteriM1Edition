/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.protection;

import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.protection.EnchantmentProtection;

public class EnchantmentProtectionExplosion
extends EnchantmentProtection {
    public EnchantmentProtectionExplosion() {
        super(3, "explosion", Enchantment.Rarity.RARE, EnchantmentProtection.TYPE.EXPLOSION);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 5 + (n - 1 << 3);
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return this.getMinEnchantAbility(n) + 12;
    }

    @Override
    public double getTypeModifier() {
        return 2.0;
    }

    @Override
    public float getProtectionFactor(EntityDamageEvent entityDamageEvent) {
        EntityDamageEvent.DamageCause damageCause = entityDamageEvent.getCause();
        if (this.level <= 0 || damageCause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && damageCause != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            return 0.0f;
        }
        return (float)((double)this.getLevel() * this.getTypeModifier());
    }
}

