/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.protection;

import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.protection.EnchantmentProtection;

public class EnchantmentProtectionFire
extends EnchantmentProtection {
    public EnchantmentProtectionFire() {
        super(1, "fire", Enchantment.Rarity.UNCOMMON, EnchantmentProtection.TYPE.FIRE);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 10 + (n - 1 << 3);
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
        if (this.level <= 0 || damageCause != EntityDamageEvent.DamageCause.LAVA && damageCause != EntityDamageEvent.DamageCause.FIRE && damageCause != EntityDamageEvent.DamageCause.FIRE_TICK) {
            return 0.0f;
        }
        return (float)((double)this.getLevel() * this.getTypeModifier());
    }
}

