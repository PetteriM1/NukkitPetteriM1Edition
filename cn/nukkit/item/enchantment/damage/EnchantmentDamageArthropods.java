/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.damage;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityArthropod;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.damage.EnchantmentDamage;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Utils;

public class EnchantmentDamageArthropods
extends EnchantmentDamage {
    public EnchantmentDamageArthropods() {
        super(11, "arthropods", Enchantment.Rarity.UNCOMMON, EnchantmentDamage.TYPE.SMITE);
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
        if (entity instanceof EntityArthropod) {
            return (double)this.getLevel() * 2.5;
        }
        return 0.0;
    }

    @Override
    public void doPostAttack(Entity entity, Entity entity2) {
        if (entity2 instanceof EntityArthropod) {
            entity2.addEffect(Effect.getEffect(2).setDuration(20 + Utils.random.nextInt(10 * this.level)).setAmplifier(3));
        }
    }
}

