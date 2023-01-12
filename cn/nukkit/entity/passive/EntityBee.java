/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntityFlyingMob;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Utils;
import java.util.HashMap;

public class EntityBee
extends EntityFlyingMob {
    public static final int NETWORK_ID = 122;
    private boolean w;

    public EntityBee(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getKillExperience() {
        return Utils.rand(1, 3);
    }

    @Override
    public int getNetworkId() {
        return 122;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.275f;
        }
        return 0.55f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.25f;
        }
        return 0.5f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(10);
        super.initEntity();
        this.setDamage(new int[]{0, 2, 2, 3});
    }

    @Override
    public double getSpeed() {
        return 1.2;
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 23 && this.distanceSquared(entity) < 1.3) {
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
            if (entity.attack(new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, hashMap))) {
                if (this.getServer().getDifficulty() == 2) {
                    entity.addEffect(Effect.getEffect(19).setDuration(200));
                } else if (this.getServer().getDifficulty() == 3) {
                    entity.addEffect(Effect.getEffect(19).setDuration(360));
                }
            }
        }
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        return this.isAngry() && super.targetOption(entityCreature, d2);
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (super.attack(entityDamageEvent)) {
            if (entityDamageEvent instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)entityDamageEvent).getDamager() instanceof Player) {
                this.setAngry(true);
            }
            return true;
        }
        return false;
    }

    public boolean isAngry() {
        return this.w;
    }

    public void setAngry(boolean bl) {
        this.w = bl;
        this.setDataFlag(0, 25, bl);
    }
}

