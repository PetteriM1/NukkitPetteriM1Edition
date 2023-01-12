/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHumanType;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;
import java.util.concurrent.ThreadLocalRandom;

public class EnchantmentThorns
extends Enchantment {
    protected EnchantmentThorns() {
        super(5, "thorns", Enchantment.Rarity.RARE, EnchantmentType.ARMOR);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 10 + (n - 1) * 20;
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return super.getMinEnchantAbility(n) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void doPostAttack(Entity entity, Entity entity2) {
        if (!(entity2 instanceof EntityHumanType) || entity == entity2) {
            return;
        }
        EntityHumanType entityHumanType = (EntityHumanType)entity2;
        int n = 0;
        for (Item item : entityHumanType.getInventory().getArmorContents()) {
            Enchantment enchantment = item.getEnchantment(5);
            if (enchantment == null) continue;
            n = Math.max(enchantment.getLevel(), n);
        }
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        if (EnchantmentThorns.b(threadLocalRandom, n)) {
            entity.attack(new EntityDamageByEntityEvent(entity2, entity, EntityDamageEvent.DamageCause.THORNS, EnchantmentThorns.a(threadLocalRandom, this.level), 0.0f));
        }
    }

    private static boolean b(ThreadLocalRandom threadLocalRandom, int n) {
        return n > 0 && (double)threadLocalRandom.nextFloat() < 0.15 * (double)n;
    }

    private static int a(ThreadLocalRandom threadLocalRandom, int n) {
        return n > 10 ? n - 10 : threadLocalRandom.nextInt(1, 5);
    }
}

