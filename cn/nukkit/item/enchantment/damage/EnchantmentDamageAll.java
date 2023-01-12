/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.damage;

import cn.nukkit.entity.Entity;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.damage.EnchantmentDamage;

public class EnchantmentDamageAll
extends EnchantmentDamage {
    public EnchantmentDamageAll() {
        super(9, "all", Enchantment.Rarity.COMMON, EnchantmentDamage.TYPE.ALL);
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
    public int getMaxEnchantableLevel() {
        return 4;
    }

    @Override
    public double getDamageBonus(Entity entity) {
        return (double)this.getLevel() * 1.25;
    }
}

