/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import java.util.HashMap;

public class EntityPiglinBrute
extends EntityWalkingMob {
    public static final int NETWORK_ID = 127;

    public EntityPiglinBrute(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(50);
        super.initEntity();
        this.setDamage(new int[]{0, 3, 7, 10});
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.95f;
    }

    @Override
    public int getKillExperience() {
        return 10;
    }

    @Override
    public int getNetworkId() {
        return 127;
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 23 && this.distanceSquared(entity) < 1.44) {
            this.attackDelay = 0;
            HashMap<EntityDamageEvent.DamageModifier, Float> hashMap = new HashMap<EntityDamageEvent.DamageModifier, Float>();
            hashMap.put(EntityDamageEvent.DamageModifier.BASE, Float.valueOf(this.getDamage()));
            if (entity instanceof Player) {
                float f2 = 0.0f;
                for (Item item : ((Player)entity).getInventory().getArmorContents()) {
                    f2 += this.getArmorPoints(item.getId());
                }
                hashMap.put(EntityDamageEvent.DamageModifier.ARMOR, Float.valueOf((float)((double)hashMap.getOrDefault((Object)EntityDamageEvent.DamageModifier.ARMOR, Float.valueOf(0.0f)).floatValue() - Math.floor((double)(hashMap.getOrDefault((Object)EntityDamageEvent.DamageModifier.BASE, Float.valueOf(1.0f)).floatValue() * f2) * 0.04))));
            }
            entity.attack(new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, hashMap));
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Piglin Brute";
    }
}

