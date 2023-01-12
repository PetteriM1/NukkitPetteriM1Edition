/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.damage;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.damage.EnchantmentDamage;

public class EnchantmentDamageSmite
extends EnchantmentDamage {
    public EnchantmentDamageSmite() {
        super(10, "undead", Enchantment.Rarity.UNCOMMON, EnchantmentDamage.TYPE.SMITE);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 5 + (n - 1 << 3);
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return this.getMinEnchantAbility(n) + 20;
    }

    @Override
    public double getDamageBonus(Entity entity) {
        if (entity instanceof EntitySmite) {
            return (double)this.getLevel() * 2.5;
        }
        return 0.0;
    }
}

