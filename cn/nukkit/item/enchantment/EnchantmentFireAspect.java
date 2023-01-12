/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityCombustByEntityEvent;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public class EnchantmentFireAspect
extends Enchantment {
    protected EnchantmentFireAspect() {
        super(13, "fire", Enchantment.Rarity.RARE, EnchantmentType.SWORD);
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
        return 2;
    }

    @Override
    public void doAttack(Entity entity, Entity entity2) {
        if (!(entity2 instanceof Player) || !((Player)entity2).isCreative()) {
            int n = Math.max(entity2.fireTicks / 20, this.getLevel() << 2);
            EntityCombustByEntityEvent entityCombustByEntityEvent = new EntityCombustByEntityEvent(entity, entity2, n);
            Server.getInstance().getPluginManager().callEvent(entityCombustByEntityEvent);
            if (!entityCombustByEntityEvent.isCancelled()) {
                entity2.setOnFire(entityCombustByEntityEvent.getDuration());
            }
        }
    }
}

